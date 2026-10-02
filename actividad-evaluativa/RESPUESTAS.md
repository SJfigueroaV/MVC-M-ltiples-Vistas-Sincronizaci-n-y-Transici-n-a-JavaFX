# Actividad Evaluativa y Preguntas para Discusión

**Integrantes:** Santiago Figueroa, Sarai Mejía y Camilo Betancourt

## 1. Pregunta conceptual

**Enunciado:** ¿por qué afirmamos que en la arquitectura moderna de JavaFX el patrón MVC se aplica de forma
más natural y forzada que en Java Swing? Mencione el rol de los archivos `.fxml` en su respuesta.

En **Swing** el MVC es una disciplina que depende del programador. La pantalla se construye con código Java
(`new JButton(...)`, `setBackground(...)`, `setLayout(...)`), y nada impide que ese mismo archivo también lea
los datos, haga cálculos y responda a los clics. El lenguaje deja mezclarlo todo en una sola clase. Por eso
en el curso hubo que **forzar** la separación a mano, creando clases `Model`, `View` y `Controller` y
cuidando que cada una hiciera solo su trabajo.

En **JavaFX** la separación viene incluida en la forma de trabajar, y el programador no tiene que imponerla:

- **La Vista es el archivo `.fxml`.** Es un documento XML (parecido al HTML) que describe *qué* hay en la
  pantalla: los botones, las tablas, los campos y cómo se acomodan. Como es XML y no Java, **no puede
  contener lógica**: no tiene `if`, ni ciclos, ni acceso al Modelo. La apariencia (colores, fuentes, bordes)
  va todavía más aparte, en una hoja de estilos **CSS**. La Vista queda "tonta" porque el formato no le
  permite otra cosa.
- **El Controlador es una clase Java que el propio `.fxml` declara** con `fx:controller="MiControlador"`.
  Al cargar la pantalla, el `FXMLLoader` crea ese controlador y le inyecta los componentes marcados con
  `@FXML` (los que tienen el mismo `fx:id`). Los eventos se enlazan desde el XML (`onAction="#guardar"`).
  El Controlador nunca dibuja la pantalla; solo la recibe ya construida y reacciona a ella.
- **El Modelo no cambia:** siguen siendo las clases y los `ArrayList` de siempre, sin saber nada de la
  interfaz.

Es **más natural** porque cada parte vive en un archivo de distinto tipo (`.fxml`, `.css`, `.java`) y cada
archivo tiene un solo propósito. Además, un diseñador puede editar el `.fxml` en Scene Builder sin tocar el
código del Controlador. Es **forzada** (en el sentido de *obligatoria*) porque la arquitectura no deja
alternativa: para usar un `.fxml` hay que tener un controlador aparte, y la Vista no tiene cómo ejecutar
lógica de negocio. En Swing el orden depende de la disciplina del equipo. En JavaFX lo impone la estructura
del proyecto.

## 2. Ejercicio práctico de sincronización

**Enunciado:** un Controlador maneja una `VistaFormulario` y una `VistaGrafico`. Complete las líneas
`// 1` y `// 2` para que la pantalla gráfica refleje el nuevo dato ingresado.

```java
public void guardarYRefrescar() {
    double nuevoDato = Double.parseDouble(vistaFormulario.getTxtDato());

    modelo.guardar(nuevoDato);

    // 1. Extraer los datos consolidados del modelo
    List<Double> todosLosDatos = modelo.getDatos();

    // 2. Enviar los datos a la VistaGrafico para que se repinte
    vistaGrafico.repintar(todosLosDatos);

    vistaFormulario.limpiarCampo();
}
```

**Explicación:**

- **// 1:** el Controlador le **pide al Modelo** la lista completa y actualizada, ya con el dato recién
  guardado. El dato sale del Modelo y no de la caja de texto, porque el Modelo es la única fuente de verdad:
  si este hubiera rechazado o transformado el valor, el gráfico lo mostraría tal como quedó guardado.
  `getDatos()` es el nombre del método de consulta del Modelo; puede llamarse distinto (por ejemplo
  `obtenerTodos()`), pero siempre es un método del `modelo` que devuelve un `List<Double>`.
- **// 2:** el Controlador le **entrega esos datos a la segunda vista**, `vistaGrafico`. Esta es la parte
  del "efecto espejo": el usuario escribió en una vista y el Controlador actualiza la otra. La
  `VistaGrafico` no conoce al Modelo ni a la `VistaFormulario`; solo recibe una lista de números y la
  dibuja.

El orden importa: primero se guarda en el Modelo, después se consulta y por último se repintan las vistas.
Si se repintara antes de guardar, el gráfico mostraría los datos viejos.

## 3. Preguntas para discusión

### Eficiencia

**Enunciado:** si tenemos una pantalla con una tabla de 10,000 registros y agregamos uno nuevo, ¿es
eficiente borrar toda la tabla (`setRowCount(0)`) y volver a repintar los 10,001 registros usando un bucle
`for`? ¿Qué alternativa lógica podríamos aplicar en el Controlador para evitar repintar todo desde cero?

**No es eficiente.** Para un solo cambio se hacen 10,001 operaciones:

- `setRowCount(0)` descarta las 10,000 filas, y el bucle vuelve a crear 10,001 arreglos `Object[]` con datos
  que ya estaban en pantalla.
- Cada `addRow` le avisa al `JTable` que hubo una inserción (`fireTableRowsInserted`). Son 10,001 eventos y
  la tabla tiene que recalcular su tamaño y repintarse una y otra vez.
- El costo crece con el tamaño de la lista (O(n) por cada venta). Con 10 filas no se nota; con miles, la
  interfaz se congela un momento después de cada clic, porque todo ocurre en el hilo de Swing.
- Además se pierde el estado visual: la fila seleccionada y la posición del scroll se reinician.

**Alternativa: actualización incremental.** El Controlador ya sabe exactamente qué cambió, porque fue él
quien le pidió al Modelo agregar el registro. Entonces solo tiene que reflejar **ese cambio**:

```java
Boleto nuevo = modelo.agregarBoleto(pelicula, sala, precio); // el Modelo devuelve lo que creó
vistaHistorial.getModeloTabla().addRow(new Object[] {           // se agrega UNA fila
    nuevo.getPelicula(), nuevo.getSala(), nuevo.getPrecio()
});
```

Así se hace una sola operación y se dispara un solo evento, sin importar si la tabla tiene 10 o 10,000
filas. Lo mismo vale para las otras operaciones: al eliminar se usa `removeRow(i)` y al editar
`setValueAt(...)` en la celda que cambió. El laboratorio CineAdmin funciona así (ver
`TaquillaController.agregarFilaHistorial`).

La misma idea sirve para los totales del dashboard: el Modelo puede llevar un **acumulado** que se actualiza
en cada venta, en lugar de recorrer los 10,001 registros cada vez que se pide el total.

Repintar todo con `setRowCount(0)` sigue siendo útil cuando cambia **toda** la lista de una vez, por ejemplo
al cargar datos, al aplicar un filtro o al reordenar. Con miles de registros también se puede crear un
`AbstractTableModel` propio que lea directamente de la lista del Modelo y avise solo el rango que cambió con
`fireTableRowsInserted(fila, fila)`, sin copiar los datos.

### Desacoplamiento estricto

**Enunciado:** algunos programadores pasan la referencia entera del Modelo a la Vista para que la Vista se
actualice "sola" leyendo los datos. ¿Por qué esta práctica (conocida como Patrón Observador pasivo) rompe
la idea de que "La Vista debe ser tonta"?

Una Vista "tonta" solo sabe **mostrar** lo que le entregan y **avisar** lo que hizo el usuario. Si recibe el
Modelo completo, empieza a tomar decisiones que no le corresponden:

1. **La Vista queda acoplada al Modelo.** Tiene que importar `TaquillaModel` y conocer sus métodos
   (`getBoletos()`, `obtenerGananciaTotal()`...). Si el Modelo cambia el nombre de un método, el tipo de su
   lista o la forma de calcular algo, la Vista se rompe aunque la pantalla no haya cambiado. Antes solo el
   Controlador dependía del Modelo; ahora dependen los dos.
2. **La Vista decide qué mostrar y cuándo.** Elige qué datos pedir, cómo recorrerlos y en qué momento
   leerlos. Esas decisiones le pertenecen al Controlador, que es el que coordina. Con varias vistas cada una
   lee el Modelo a su manera y en su propio momento, y ya no hay un único punto que garantice que todas
   estén sincronizadas.
3. **La lógica se cuela en la Vista.** Una vez que la Vista tiene el Modelo, es muy fácil que termine
   haciendo cálculos (sumar ventas, filtrar, validar) o incluso modificándolo (`modelo.agregarBoleto(...)`
   desde un botón). Eso salta al Controlador y deja reglas de negocio repartidas en la interfaz.
4. **La Vista no se puede reutilizar ni probar sola.** Un `PanelDashboard` que recibe dos textos sirve para
   cualquier sistema y se prueba sin datos reales. Uno que recibe un `TaquillaModel` solo sirve para la
   taquilla y necesita un Modelo armado para funcionar.

Con el Controlador como orquestador, el flujo va en un solo sentido: el Controlador lee el Modelo, prepara
los datos y se los entrega ya listos a cada Vista (`vistaDashboard.setTotalVendido("Total Vendido:
$35.000")`). La Vista no sabe de dónde salió ese texto, y por eso es "tonta".

Vale aclarar que en el MVC clásico (el de Smalltalk) la Vista sí observaba al Modelo. Lo que se
cuestiona aquí es esa variante, comparada con la que usamos en el curso (llamada *Passive View*), donde
la Vista es pasiva y todo pasa por el Controlador. Esta segunda es la que deja las vistas desacopladas,
fáciles de cambiar y fáciles de probar.
