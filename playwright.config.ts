import { defineConfig } from '@playwright/test';

/**
 * ATDD contra la API REST. La app debe estar levantada (perfil recomendado: `test` con H2).
 * @see GUIA_DESDE_CERO/05_DESARROLLO_TDD_BDD_ATDD.md
 */
const baseURL = process.env.BASE_URL ?? 'http://localhost:8080';

export default defineConfig({
  testDir: './tests/atdd',
  fullyParallel: true,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 1 : 0,
  reporter: [['list']],
  use: {
    baseURL,
    extraHTTPHeaders: {
      Accept: 'application/json',
      'Content-Type': 'application/json',
    },
  },
});
