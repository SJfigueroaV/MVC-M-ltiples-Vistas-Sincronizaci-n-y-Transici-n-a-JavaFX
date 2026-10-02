# Laboratorio práctico: Sistema "CineAdmin" — Dashboard Multi-Panel

Aplicación de escritorio en Java Swing con el patrón MVC. Un panel de registro alimenta de datos, al mismo
tiempo, una tabla de historial y un panel de métricas en tiempo real.

## Ejecutar

```bash
javac -d out $(find src -name '*.java') && java -cp out Main
```

## Estructura

```
src/
├── Main.java                      Crea el Modelo y la ventana, y se los entrega al Controlador
├── modelo/
│   ├── Boleto.java                Película, sala y precio (inmutable)
│   └── TaquillaModel.java         ArrayList<Boleto>, agregarBoleto, obtenerGananciaTotal, obtenerCantidadBoletos
├── vista/
│   ├── MainFrame.java             JFrame con BorderLayout: WEST, CENTER y SOUTH
│   ├── PanelIngreso.java          Izquierda: cajas de texto y botón "Vender"
│   ├── PanelHistorial.java        Centro: JTable dentro de un JScrollPane
│   └── PanelDashboard.java        Abajo: "Total Vendido" y "Boletos Emitidos"
└── controlador/
    └── TaquillaController.java    Orquestador: recibe el Modelo y las tres vistas
```

## Qué pasa al hacer clic en "Vender"

1. El Controlador lee las cajas de texto del `PanelIngreso` y valida que no estén vacías y que el precio
   sea numérico.
2. **a)** Crea el boleto en el Modelo con `agregarBoleto(...)`. El Modelo aplica las reglas de negocio
   (película y sala obligatorias, precio mayor que cero) y devuelve el boleto creado.
3. **b)** Agrega **solo esa fila nueva** al `DefaultTableModel` del `PanelHistorial` con `addRow`. No borra
   ni repinta la tabla completa (ver la pregunta de eficiencia en
   [`../actividad-evaluativa/RESPUESTAS.md`](../actividad-evaluativa/RESPUESTAS.md)).
4. **c)** Le pide al Modelo los nuevos totales y hace `setText` en los labels del `PanelDashboard`.
5. Limpia las cajas de texto para la siguiente venta.

Las tres regiones quedan sincronizadas en el mismo clic: el Modelo es el "marcador oficial" y el
Controlador se encarga de que todas las pantallas lo reflejen.

## Decisiones de diseño

- **Las vistas son "tontas".** Ningún panel conoce al Modelo ni a los demás paneles. Solo muestran lo que
  les entrega el Controlador y le entregan a este lo que escribe el usuario.
- **El Controlador recibe todas las vistas en su constructor**, igual que el `OrquestadorController` de la
  clase. `MainFrame` solo arma la ventana y expone sus tres paneles.
- **El Modelo lleva un acumulado de las ventas**, así `obtenerGananciaTotal()` no recorre toda la lista en
  cada venta.
- **El Modelo no importa `javax.swing`** y entrega la lista en modo solo lectura (`getBoletos()`).
- Los precios se muestran en pesos con separador de miles (`$12.000`).
