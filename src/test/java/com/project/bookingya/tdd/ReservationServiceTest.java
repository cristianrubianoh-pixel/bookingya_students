package com.project.bookingya.tdd;

import com.project.bookingya.dtos.ReservationDto;
import com.project.bookingya.entities.GuestEntity;
import com.project.bookingya.entities.ReservationEntity;
import com.project.bookingya.entities.RoomEntity;
import com.project.bookingya.models.Reservation;
import com.project.bookingya.repositories.IGuestRepository;
import com.project.bookingya.repositories.IReservationRepository;
import com.project.bookingya.repositories.IRoomRepository;
import com.project.bookingya.services.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) //Permite que se pueda activar los mocks que vienen en mockito
 public class ReservationServiceTest {

    @Mock
    IReservationRepository reservationRepository;  // Llamado de los Mock de la BD
    @Mock
    IRoomRepository roomRepository;
    @Mock
    IGuestRepository guestRepository;

    private ReservationService reservationService; // Es el objeto que deseamos probar

    private UUID reservationId; //  el ID que tendrá la reserva cuando se guarde en BD
    private UUID roomId; // el ID de la habitación
    private UUID guestId; // el ID del huésped
    private LocalDateTime checkIn; // fecha de entrada;
    private LocalDateTime checkOut; // fecha de salida

    private RoomEntity room; //  la habitación que existe en el sistema
    private GuestEntity guest; // el huésped que existe en el sistema
    private ReservationEntity entity; // la reserva como la guarda la BD (con ID)
    private ReservationDto dto; //  los datos que manda el cliente para crear la reserva (sin ID)


    private ModelMapper createModelMapper() {
        ModelMapper mapper = new ModelMapper();
        // Modo estricto: solo mapea campos con nombres exactamente iguales
        mapper.getConfiguration()
            .setMatchingStrategy(org.modelmapper.convention.MatchingStrategies.STRICT);
        return mapper;
    }

    @BeforeEach
    void setUp() {
        reservationService = new ReservationService(
                reservationRepository,
                roomRepository,
                guestRepository,
                createModelMapper()
        );

        reservationId = UUID.randomUUID();
        roomId = UUID.randomUUID();
        guestId = UUID.randomUUID();
        checkIn = LocalDateTime.now().plusDays(1);
        checkOut = checkIn.plusDays(3);

        // Habitación de prueba disponible con capacidad para 3 personas
        room = new RoomEntity();
        room.setId(roomId);
        room.setCode("HAB-TEST-01");
        room.setName("Habitación de Prueba");
        room.setCity("Bogotá");
        room.setMaxGuests(3);
        room.setNightlyPrice(new BigDecimal("150000"));
        room.setAvailable(true);

        // Huésped de prueba registrado en el sistema
        guest = new GuestEntity();
        guest.setId(guestId);
        guest.setIdentification("123456789");
        guest.setName("Juan Pérez");
        guest.setEmail("juan@test.com");

        // Reserva de prueba como la guardaría la base de datos (con ID)
        entity = new ReservationEntity();
        entity.setId(reservationId);
        entity.setGuestId(guestId);
        entity.setRoomId(roomId);
        entity.setCheckIn(checkIn);
        entity.setCheckOut(checkOut);
        entity.setGuestsCount(2);
        entity.setNotes("Reserva de prueba");

        // DTO - datos que envía el cliente para crear la reserva (sin ID)
        dto = new ReservationDto();
        dto.setGuestId(guestId);
        dto.setRoomId(roomId);
        dto.setCheckIn(checkIn);
        dto.setCheckOut(checkOut);
        dto.setGuestsCount(2);
        dto.setNotes("Reserva de prueba");
    }

    @Test
    void deberiaCrearUnaReservaExitosamente() {

        // Arrange — preparamos los mocks para simular una creación exitosa
        when(roomRepository.findById(roomId))
                .thenReturn(Optional.of(room));                                           // simula que la habitación existe en la BD

        when(guestRepository.findById(guestId))
                .thenReturn(Optional.of(guest));                                          // simula que el huésped existe en la BD

        when(reservationRepository.existsOverlappingReservationForRoom(any(), any(), any(), any()))
                .thenReturn(false);                                                       // simula que no hay reservas solapadas para la habitación

        when(reservationRepository.existsOverlappingReservationForGuest(any(), any(), any(), any()))
                .thenReturn(false);                                                       // simula que el huésped no tiene otra reserva en ese rango

        when(reservationRepository.saveAndFlush(any(ReservationEntity.class)))
                .thenReturn(entity);                                                      // simula que la BD guarda y devuelve la reserva con ID asignado

        // Act — ejecutamos el método que queremos probar
        Reservation resultado = reservationService.create(dto);                           // llamamos a create con el DTO del cliente

        // Assert — verificamos que el resultado sea el esperado
        assertNotNull(resultado);                                                         // la reserva creada no debe ser nula
        assertEquals(reservationId, resultado.getId());                                   // el ID retornado debe coincidir con el que asignó la BD
    }

    @Test
    void deberiaRetornarTodasLasReservas() {

        // Arrange — preparamos el mock para que devuelva una lista con una reserva
        when(reservationRepository.findAll())
                .thenReturn(List.of(entity));                                             // simula que la BD tiene exactamente una reserva guardada

        // Act — ejecutamos el método que queremos probar
        List<Reservation> resultado = reservationService.getAll();                        // llamamos a getAll y capturamos la lista devuelta

        // Assert — verificamos que la lista sea la esperada
        assertNotNull(resultado);                                                         // la lista no debe ser nula
        assertEquals(1, resultado.size());                                                // debe haber exactamente un elemento en la lista
        assertEquals(reservationId, resultado.get(0).getId());                            // el ID del primer elemento debe coincidir con el de la BD
    }

    @Test
    void deberiaRetornarReservaPorId() {

        // Arrange — preparamos el mock para que devuelva la reserva cuando se busca por ID
        when(reservationRepository.findById(reservationId))
                .thenReturn(Optional.of(entity));                                         // simula que la BD encuentra la reserva con ese ID

        // Act — ejecutamos el método que queremos probar
        Reservation resultado = reservationService.getById(reservationId);                // buscamos la reserva por su ID

        // Assert — verificamos que los datos devueltos sean los esperados
        assertNotNull(resultado);                                                         // la reserva encontrada no debe ser nula
        assertEquals(reservationId, resultado.getId());                                   // el ID debe coincidir con el que buscamos
        assertEquals(guestId, resultado.getGuestId());                                    // el huésped de la reserva debe ser el mismo que se guardó
        assertEquals(roomId, resultado.getRoomId());                                      // la habitación de la reserva debe ser la misma que se guardó
    }

    @Test
    void deberiaActualizarUnaReservaExitosamente() {

        // Arrange — preparamos los mocks para simular una actualización exitosa
        when(reservationRepository.findById(reservationId))
                .thenReturn(Optional.of(entity));                                         // simula que la reserva a actualizar existe en la BD

        when(roomRepository.findById(roomId))
                .thenReturn(Optional.of(room));                                           // simula que la habitación asociada existe en la BD

        when(guestRepository.findById(guestId))
                .thenReturn(Optional.of(guest));                                          // simula que el huésped asociado existe en la BD

        when(reservationRepository.existsOverlappingReservationForRoom(any(), any(), any(), any()))
                .thenReturn(false);                                                       // simula que no hay solapamiento de fechas para la habitación

        when(reservationRepository.existsOverlappingReservationForGuest(any(), any(), any(), any()))
                .thenReturn(false);                                                       // simula que el huésped no tiene otra reserva en ese rango

        when(reservationRepository.saveAndFlush(any(ReservationEntity.class)))
                .thenReturn(entity);                                                      // simula que la BD guarda los cambios y devuelve la reserva actualizada

        // Act — ejecutamos el método que queremos probar
        Reservation resultado = reservationService.update(dto, reservationId);            // llamamos a update con el DTO y el ID de la reserva a modificar

        // Assert — verificamos que el resultado sea el esperado
        assertNotNull(resultado);                                                         // la reserva actualizada no debe ser nula
        assertEquals(reservationId, resultado.getId());                                   // el ID debe seguir siendo el mismo después de la actualización
    }

    @Test
    void deberiaEliminarUnaReserva() {

        // Arrange — preparamos los mocks para simular una eliminación exitosa
        when(reservationRepository.findById(reservationId))
                .thenReturn(Optional.of(entity));                                         // simula que la reserva a eliminar existe en la BD

        doNothing().when(reservationRepository).delete(any());                            // simula que delete no lanza excepciones ni tiene efectos secundarios

        // Act — ejecutamos el método que queremos probar
        reservationService.delete(reservationId);                                         // llamamos a delete con el ID de la reserva a eliminar

        // Assert — verificamos que delete fue invocado exactamente una vez con la entidad correcta
        verify(reservationRepository, times(1)).delete(entity);                           // confirma que la BD recibió exactamente una llamada a delete con nuestra entidad
    }

}
