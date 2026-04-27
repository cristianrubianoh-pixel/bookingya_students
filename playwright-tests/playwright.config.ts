import { defineConfig } from '@playwright/test';

export default defineConfig({
  // Carpeta donde están los tests
  testDir: './tests',

  // Tiempo máximo por test: 30 segundos
  timeout: 30000,

  // Reportes — lista en consola y HTML
  reporter: [
    ['list'],
    ['html', { outputFolder: 'playwright-report', open: 'never' }]
  ],

  use: {
    // URL base de la API — debe estar corriendo en localhost:8080
    baseURL: 'http://localhost:8080/api',

    // Captura info extra cuando falla
    trace: 'on-first-retry',
  },
});
