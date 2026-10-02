# Taller: MVC Avanzado — Múltiples Vistas, Sincronización y Transición a JavaFX

Fundamentos de Diseño de Software
Programa Ciencias de la Computación e Inteligencia Artificial
Escuela de Ciencias Exactas e Ingeniería — Universidad Sergio Arboleda

**Integrantes:**

- Santiago Figueroa
- Sarai Mejía
- Camilo Betancourt

Aplicación de escritorio en Java Swing donde un solo Controlador mantiene sincronizadas varias vistas a
partir de un mismo Modelo (el "efecto espejo"), junto con las respuestas de la pregunta conceptual sobre
JavaFX, el ejercicio de sincronización y las preguntas para discusión.

## Contenido

| Carpeta | Punto del taller |
|---|---|
| [`laboratorio-cineadmin/`](laboratorio-cineadmin/) | Laboratorio práctico: Sistema "CineAdmin" — Dashboard Multi-Panel |
| [`actividad-evaluativa/`](actividad-evaluativa/) | Pregunta conceptual (Swing vs. JavaFX), ejercicio práctico de sincronización y preguntas para discusión |

## Requisitos

Un JDK (Java 8 o superior). No se necesitan librerías externas: Java Swing viene incluido en el JDK.

## Ejecutar

```bash
cd laboratorio-cineadmin && javac -d out $(find src -name '*.java') && java -cp out Main
```

## El patrón aplicado

- **El Modelo** (el "marcador oficial") guarda el `ArrayList<Boleto>` y las reglas de negocio, y nunca
  importa `javax.swing`.
- **Las Vistas** (la pantalla del estadio, la TV, el celular) son tres paneles independientes: ingreso,
  historial y dashboard. Ninguno conoce al Modelo ni a los otros paneles.
- **El Controlador orquestador** recibe el Modelo y **todas** las vistas en su constructor. Con cada venta
  actualiza el Modelo y, en el mismo clic, le agrega la fila nueva a la tabla y refresca las métricas del
  dashboard.
