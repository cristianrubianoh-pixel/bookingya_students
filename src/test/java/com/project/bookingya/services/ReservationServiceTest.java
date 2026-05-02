package com.project.bookingya.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;

import com.project.bookingya.dtos.ReservationDto;
import com.project.bookingya.entities.GuestEntity;
import com.project.bookingya.entities.ReservationEntity;
import com.project.bookingya.entities.RoomEntity;
import com.project.bookingya.exceptions.BusinessRuleException;
import com.project.bookingya.exceptions.EntityNotExistsException;
import com.project.bookingya.models.Reservation;
import com.project.bookingya.repositories.IGuestRepository;
import com.project.bookingya.repositories.IReservationRepository;
import com.project.bookingya.repositories.IRoomRepository;
import com.project.bookingya.shared.Constants;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private IReservationRepository reservationRepository;

    @Mock
    private IRoomRepository roomRepository;

    @Mock
    private IGuestRepository guestRepository;

    @Mock
    private ModelMapper mapper;

    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationService = new ReservationService(
            reservationRepository,
            roomRepository,
            guestRepository,
            mapper
        );
    }

    @SuppressWarnings("null")
    @Test
    void create_shouldCreateReservationSuccessfully() {
        UUID roomId = UUID.randomUUID();
        UUID guestId = UUID.randomUUID();
        UUID reservationId = UUID.randomUUID();

        ReservationDto dto = buildValidDto(roomId, guestId);
        stubValidRoom(roomId);
        stubValidGuest(guestId);
        stubNoOverlaps(roomId, guestId, dto, null);

        ReservationEntity savedEntity = Objects.requireNonNull(buildEntity(reservationId, dto));
        when(mapper.map(dto, ReservationEntity.class)).thenReturn(savedEntity);
        when(mapper.map(savedEntity, Reservation.class)).thenReturn(toReservation(savedEntity));
        when(reservationRepository.saveAndFlush(savedEntity)).thenReturn(savedEntity);

        Reservation result = reservationService.create(dto);

        assertNotNull(result);
        assertEquals(reservationId, result.getId());
        assertEquals(roomId, result.getRoomId());
        assertEquals(guestId, result.getGuestId());
        verify(reservationRepository).saveAndFlush(savedEntity);
        verify(reservationRepository, never()).delete(any());
        assertTrue(result.getCheckIn().isBefore(result.getCheckOut()));
    }

    @SuppressWarnings("null")
    @Test
    void create_shouldThrow_whenDateRangeInvalid() {
        UUID roomId = UUID.randomUUID();
        UUID guestId = UUID.randomUUID();
        ReservationDto dto = buildValidDto(roomId, guestId);
        dto.setCheckIn(LocalDateTime.of(2026, 5, 12, 11, 0));
        dto.setCheckOut(LocalDateTime.of(2026, 5, 10, 14, 0));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> reservationService.create(dto));
        assertEquals(Constants.INVALID_RESERVATION_RANGE, ex.getMessage());
        verify(reservationRepository, never()).saveAndFlush(any());
    }

    @Test
    void getById_shouldReturnReservationWhenExists() {
        UUID id = UUID.randomUUID();
        ReservationEntity entity = new ReservationEntity();
        entity.setId(id);
        entity.setGuestId(UUID.randomUUID());
        entity.setRoomId(UUID.randomUUID());
        entity.setCheckIn(LocalDateTime.of(2026, 5, 1, 14, 0));
        entity.setCheckOut(LocalDateTime.of(2026, 5, 3, 11, 0));
        entity.setGuestsCount(1);
        entity.setNotes("x");

        when(reservationRepository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.map(entity, Reservation.class)).thenReturn(toReservation(entity));

        Reservation result = reservationService.getById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(reservationRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotExistsException ex = assertThrows(EntityNotExistsException.class, () -> reservationService.getById(id));
        assertEquals(Constants.RESERVATION_NOT_FOUND, ex.getMessage());
    }

    @Test
    void getAll_shouldReturnMappedList() {
        ReservationEntity e1 = buildEntity(UUID.randomUUID(), buildValidDto(UUID.randomUUID(), UUID.randomUUID()));
        when(reservationRepository.findAll()).thenReturn(List.of(e1));
        @SuppressWarnings("unchecked")
        List<Reservation> expected = List.of(toReservation(e1));
        when(mapper.map(eq(List.of(e1)), ArgumentMatchers.<Type>any())).thenReturn(expected);

        List<Reservation> result = reservationService.getAll();

        assertEquals(1, result.size());
        assertEquals(expected.get(0).getId(), result.get(0).getId());
    }

    @Test
    void getByRoomId_shouldReturnMappedList() {
        UUID roomId = UUID.randomUUID();
        ReservationDto dto = buildValidDto(roomId, UUID.randomUUID());
        ReservationEntity e1 = buildEntity(UUID.randomUUID(), dto);
        when(reservationRepository.findByRoomId(roomId)).thenReturn(List.of(e1));

        List<Reservation> mapped = List.of(toReservation(e1));
        when(mapper.map(eq(List.of(e1)), ArgumentMatchers.<Type>any())).thenReturn(mapped);

        assertEquals(1, reservationService.getByRoomId(roomId).size());
    }

    @Test
    void getByGuestId_shouldReturnMappedList() {
        UUID guestId = UUID.randomUUID();
        ReservationDto dto = buildValidDto(UUID.randomUUID(), guestId);
        ReservationEntity e1 = buildEntity(UUID.randomUUID(), dto);
        when(reservationRepository.findByGuestId(guestId)).thenReturn(List.of(e1));

        List<Reservation> mapped = List.of(toReservation(e1));
        when(mapper.map(eq(List.of(e1)), ArgumentMatchers.<Type>any())).thenReturn(mapped);

        assertEquals(1, reservationService.getByGuestId(guestId).size());
    }

    @Test
    void update_shouldUpdateReservationSuccessfully() {
        UUID reservationId = UUID.randomUUID();
        UUID roomId = UUID.randomUUID();
        UUID guestId = UUID.randomUUID();

        ReservationDto dto = buildValidDto(roomId, guestId);
        ReservationEntity existing = buildEntity(reservationId, dto);
        existing.setNotes("vieja");

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(existing));
        stubValidRoom(roomId);
        stubValidGuest(guestId);
        stubNoOverlaps(roomId, guestId, dto, reservationId);

        doAnswer((Answer<Void>) inv -> {
            ReservationDto src = inv.getArgument(0);
            ReservationEntity dest = inv.getArgument(1);
            dest.setCheckIn(src.getCheckIn());
            dest.setCheckOut(src.getCheckOut());
            dest.setGuestsCount(src.getGuestsCount());
            dest.setGuestId(src.getGuestId());
            dest.setRoomId(src.getRoomId());
            dest.setNotes(src.getNotes());
            return null;
        }).when(mapper).map(any(ReservationDto.class), any(ReservationEntity.class));

        when(reservationRepository.saveAndFlush(existing)).thenReturn(existing);
        when(mapper.map(any(ReservationEntity.class), eq(Reservation.class))).thenAnswer(inv -> toReservation(inv.getArgument(0)));

        dto.setNotes("actualizada");

        Reservation result = reservationService.update(dto, reservationId);

        assertEquals("actualizada", result.getNotes());
        verify(reservationRepository).saveAndFlush(existing);
    }

    @Test
    void update_shouldThrow_whenReservationNotFound() {
        UUID id = UUID.randomUUID();
        when(reservationRepository.findById(id)).thenReturn(Optional.empty());

        ReservationDto dto = buildValidDto(UUID.randomUUID(), UUID.randomUUID());

        EntityNotExistsException ex = assertThrows(
            EntityNotExistsException.class,
            () -> reservationService.update(dto, id)
        );
        assertEquals(Constants.RESERVATION_NOT_FOUND, ex.getMessage());
    }

    @Test
    void delete_shouldDeleteReservationWhenExists() {
        UUID id = UUID.randomUUID();
        ReservationEntity entity = buildEntity(id, buildValidDto(UUID.randomUUID(), UUID.randomUUID()));
        when(reservationRepository.findById(id)).thenReturn(Optional.of(entity));

        assertDoesNotThrow(() -> reservationService.delete(id));
        verify(reservationRepository).delete(entity);
        verify(reservationRepository).flush();
    }

    @Test
    void delete_shouldThrow_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(reservationRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotExistsException ex = assertThrows(EntityNotExistsException.class, () -> reservationService.delete(id));
        assertEquals(Constants.RESERVATION_NOT_FOUND, ex.getMessage());
    }

    private void stubValidRoom(UUID roomId) {
        RoomEntity room = new RoomEntity();
        room.setId(roomId);
        room.setCode("HAB-101");
        room.setName("Habitacion Doble");
        room.setCity("Bogota");
        room.setMaxGuests(2);
        room.setNightlyPrice(new BigDecimal("150000"));
        room.setAvailable(true);
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
    }

    private void stubValidGuest(UUID guestId) {
        GuestEntity guest = new GuestEntity();
        guest.setId(guestId);
        guest.setIdentification("CC12345678");
        guest.setName("Juan Perez");
        guest.setEmail("juan.perez@example.com");
        when(guestRepository.findById(guestId)).thenReturn(Optional.of(guest));
    }

    private void stubNoOverlaps(UUID roomId, UUID guestId, ReservationDto dto, UUID excludeReservationId) {
        when(reservationRepository.existsOverlappingReservationForRoom(
            eq(roomId), eq(dto.getCheckIn()), eq(dto.getCheckOut()), eq(excludeReservationId)
        )).thenReturn(false);
        when(reservationRepository.existsOverlappingReservationForGuest(
            eq(guestId), eq(dto.getCheckIn()), eq(dto.getCheckOut()), eq(excludeReservationId)
        )).thenReturn(false);
    }

    private ReservationDto buildValidDto(UUID roomId, UUID guestId) {
        ReservationDto dto = new ReservationDto();
        dto.setRoomId(roomId);
        dto.setGuestId(guestId);
        dto.setCheckIn(LocalDateTime.of(2026, 5, 10, 14, 0));
        dto.setCheckOut(LocalDateTime.of(2026, 5, 12, 11, 0));
        dto.setGuestsCount(2);
        dto.setNotes("Reserva inicial de prueba");
        return dto;
    }

    private ReservationEntity buildEntity(UUID reservationId, ReservationDto dto) {
        ReservationEntity entity = new ReservationEntity();
        entity.setId(reservationId);
        entity.setRoomId(dto.getRoomId());
        entity.setGuestId(dto.getGuestId());
        entity.setCheckIn(dto.getCheckIn());
        entity.setCheckOut(dto.getCheckOut());
        entity.setGuestsCount(dto.getGuestsCount());
        entity.setNotes(dto.getNotes());
        return entity;
    }

    private Reservation toReservation(ReservationEntity entity) {
        Reservation model = new Reservation();
        model.setId(entity.getId());
        model.setGuestId(entity.getGuestId());
        model.setRoomId(entity.getRoomId());
        model.setCheckIn(entity.getCheckIn());
        model.setCheckOut(entity.getCheckOut());
        model.setGuestsCount(entity.getGuestsCount());
        model.setNotes(entity.getNotes());
        return model;
    }
}
