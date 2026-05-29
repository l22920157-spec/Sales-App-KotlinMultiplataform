# Bitácora de Proyecto: Aplicación móvil en al menos 2 plataformas (Android y iOS) 🚀

**Nombre del Proyecto:** SalesApp (KMP1)
**Tecnología Principal:** Kotlin Multiplatform (KMP) y Compose Multiplatform
**Plataformas Soportadas:** Android, iOS, Web (WasmJS / JS)
**Arquitectura:** Clean Architecture + Offline-First

---

## 1. Introducción y Contexto del Proyecto

El desarrollo de software en la actualidad exige una presencia omnicanal. Tradicionalmente, construir una aplicación para dispositivos Apple, otra para el ecosistema Android y una versión accesible desde navegadores web implicaba conformar tres equipos de desarrollo distintos. Esto se traduce en altos costos de mantenimiento, bases de código duplicadas y una propensión a errores asimétricos, donde una plataforma recibe actualizaciones más rápido que las demás.

Para dar solución a esta problemática, el presente proyecto se centra en el desarrollo de **SalesApp**, un sistema integral de gestión enfocado en el control de clientes y catálogo de productos, construido enteramente bajo el ecosistema de **Kotlin Multiplatform (KMP)**.

### Objetivos Principales
1. **Unificación de Código:** El objetivo arquitectónico primordial es escribir la lógica de negocio (casos de uso), la persistencia de datos y la interfaz gráfica (UI) **una sola vez**, compartiendo este código de manera nativa entre plataformas móviles (Android e iOS) y navegadores web de escritorio a través de WebAssembly (WasmJS).
2. **Independencia de la Conexión (Offline-First):** Se busca proveer una aplicación robusta que permita a los usuarios (ej. agentes de ventas) consultar, registrar y modificar información en zonas sin cobertura de internet, sincronizando automáticamente la base de datos local con un servidor remoto (API) una vez que la conexión sea restablecida.
3. **Alto Rendimiento:** A diferencia de frameworks híbridos basados en WebView (como Cordova o Ionic), este proyecto busca generar binarios compilados de forma nativa para los dispositivos móviles y aprovechar la velocidad de ejecución de memoria de WebAssembly en navegadores, garantizando transiciones suaves a 60 FPS y una experiencia de usuario (UX) inmejorable.

## 2. Stack Tecnológico Seleccionado
Para garantizar compatibilidad y rendimiento, se eligieron herramientas 100% compatibles con el paradigma "Multiplatform":
- **UI Framework:** Compose Multiplatform (Comparte UI en Android, iOS y Web).
- **Inyección de Dependencias (DI):** Koin (Reemplazó a Hilt/Dagger, que son exclusivos de JVM).
- **Consumo de APIs / Networking:** Ktor Client 3.x (Reemplazó a Retrofit).
- **Serialización de Datos:** Kotlinx.Serialization (Reemplazó a Gson).
- **Persistencia Local (BD):** SQLDelight (Genera SQL seguro y soporta SQLite en Android/iOS y sql.js en Web).

---

## 3. Fases del Desarrollo

### Fase 1: Configuración del Entorno KMP
- Se generó el proyecto base utilizando la plantilla de Kotlin Multiplatform.
- Se definieron los módulos principales: `commonMain` (código compartido), `androidMain`, `iosMain`, y `wasmJsMain`.
- Se configuraron las dependencias en `libs.versions.toml` para estandarizar las versiones (Koin 4.x, Ktor 3.1.3, SQLDelight 2.1.0).

### Fase 2: Desarrollo de la Lógica de Dominio y Datos (Networking)
- **Modelos:** Se definieron las entidades de dominio: `Product` y `Customer`.
- **Ktor Client:** Se configuró un cliente HTTP centralizado para apuntar a un servidor backend (`Elysia.js` corriendo en el puerto 3000 de la red local).
- **Serialización:** Se aplicó la etiqueta `@Serializable` a los Data Transfer Objects (DTOs) para manejar la entrada y salida de JSON de manera eficiente.

### Fase 3: Persistencia Local (Offline-First)
- Se descartó Room debido a su limitación con ecosistemas fuera de Android. Se implementó **SQLDelight**.
- Se programaron los archivos `.sq` (`ProductEntity.sq` y `CustomerEntity.sq`) que generaron interfaces de Kotlin automáticamente.
- **Factoría de Controladores:** 
  - Android utiliza `AndroidSqliteDriver`.
  - iOS utiliza `NativeSqliteDriver`.
  - Web utiliza `WebWorkerDriver` (asíncrono).

### Fase 4: Integración del Patrón Repositorio
## 3. Arquitectura y Estructura del Proyecto

El proyecto está diseñado bajo los principios de **Clean Architecture**, dividiendo las responsabilidades en capas claramente separadas para hacer el código escalable y testeable. 

### 3.1. Capas Lógicas (Clean Architecture)
Dentro del módulo `shared` (código compartido), el sistema se organiza en:
1. **Dominio (`domain`):** Es el corazón de la aplicación. Contiene los Modelos de negocio y las interfaces de los Repositorios. No tiene dependencias externas.
2. **Datos (`data`):** Se encarga de proveer información a la capa de dominio manejando lógica **Offline-First**. Se divide en:
   - *Remote:* Llamadas a la API vía Ktor.
   - *Local:* Fuentes de datos locales vía SQLDelight.
3. **Inyección de Dependencias (`di`):** Administra cómo se crean y proveen las clases en todo el sistema utilizando Koin.
4. **Presentación (`presentation` / UI):** Capa visual desarrollada enteramente en **Compose Multiplatform**. Contiene Pantallas y ViewModels.

### 3.2. Estructura de Módulos Físicos (Source Sets de KMP)
```text
📦 composeApp
 ┣ 📂 src
 ┃ ┣ 📂 commonMain        # (90% del código) Dominio, Datos, UI en Compose y Koin.
 ┃ ┣ 📂 androidMain       # Código específico para Android (Base de Datos SQLite).
 ┃ ┣ 📂 iosMain           # Código específico para iOS (NativeSqliteDriver).
 ┃ ┣ 📂 wasmJsMain        # Código específico para WebAssembly (WebWorkerDriver para SQL.js).
 ┃ ┗ 📂 jsMain            # Código específico para Javascript.
 ┗ 📜 build.gradle.kts    # Configuración de dependencias.
```
> 📷 **[Insertar captura de pantalla de la estructura de carpetas del IDE aquí]**

---

## 4. Desarrollo por Fases e Implementación (Código)

### Fase 1: Configuración de Dependencias (Gradle)
Se configuraron las dependencias en `libs.versions.toml` y `build.gradle.kts` para estandarizar las herramientas en los módulos compartidos y plataformas específicas.
> 📷 **[Insertar captura de código de `build.gradle.kts` o `libs.versions.toml` (sección dependencias) aquí]**

### Fase 2: Implementación de la Capa de Dominio
Se definieron las entidades clave (`Product`, `Customer`) y las interfaces de sus repositorios para abstraer de dónde provienen los datos.
> 📷 **[Insertar captura de código del Data Class `Product.kt` o `Customer.kt` aquí]**

### Fase 3: Capa de Red y API Remota (Ktor Client)
Se configuró un cliente HTTP centralizado para apuntar a un servidor backend (`Elysia.js` corriendo en el puerto 3000 de la red local). Se mapearon las respuestas JSON a DTOs.
> 📷 **[Insertar captura de código de `HttpClientFactory.kt` o `ProductApiService.kt` aquí]**

### Fase 4: Persistencia Local Offline-First (SQLDelight)
Se implementó SQLDelight creando las tablas desde archivos `.sq` y configurando el Factory que inyecta la base de datos dependiendo si el entorno es Móvil o Web.
> 📷 **[Insertar captura de código de `ProductEntity.sq` o de `DatabaseDriverFactory.kt` aquí]**

### Fase 5: El Patrón Repositorio y Sincronización
Se programó el puente entre Ktor y SQLDelight. Al abrir la app, la UI se suscribe a los cambios locales, mientras que en paralelo el repositorio consulta la web y actualiza la tabla si hay cambios (flujo reactivo).
> 📷 **[Insertar captura de código de `ProductRepositoryImpl.kt` mostrando la función `emitAll()` y el `try-catch` aquí]**

### Fase 6: Inyección de Dependencias (Koin)
Se configuraron los módulos compartidos para proveer (inyectar) el repositorio y los ViewModels en la UI sin tener que instanciarlos de forma manual.
> 📷 **[Insertar captura de código de `AppModule.kt` o `RepositoryModule.kt` aquí]**

### Fase 7: Diseño de UI y ViewModels (Compose Multiplatform)
Se desarrollaron las pantallas usando **Material 3**. La UI escucha el estado provisto por el `ViewModel` (Cargando, Éxito, Error) e infla listas de elementos compartiendo 100% el diseño en Android, iOS y Web.
> 📷 **[Insertar captura de código de `ProductScreen.kt` o de un componente de Compose aquí]**
> 📷 **[Insertar captura de cómo se ve la interfaz de la aplicación corriendo (Emulador Android o Navegador) aquí]**

---

## 5. Retos Técnicos y Soluciones (Troubleshooting)

Durante la integración con la plataforma **WebAssembly (WasmJS)**, superamos retos arquitectónicos profundos del ecosistema Web:

1. **Error de Renderizado Shadow DOM (`attachShadow`):** Compose WasmJS no soportaba el montaje directo en un `<canvas>` dinámico. Se solucionó actualizando el `index.html` para incluir `<div id="ComposeTarget"></div>`.
> 📷 **[Insertar captura de código de `index.html` modificado aquí]**

2. **Bloqueos por Base de Datos Síncrona en WasmJS:** SQLDelight funciona mediante Web Workers en la web. Dado que los Workers fallan al ser consultados sincrónicamente, habilitamos `generateAsync.set(true)` y migramos los `DataSources` a llamadas asíncronas (`.awaitAsList()`) con bucles iterativos.
> 📷 **[Insertar captura de código de `ProductLocalDataSource.kt` o la configuración async en `build.gradle.kts` aquí]**

3. **Ausencia del binario WebAssembly (`magic word 00 61 73 6d`):** El servidor Webpack no encontraba el archivo `sql-wasm.wasm`. Se resolvió configurando explícitamente el `devServer` en Gradle y empaquetando el archivo en la carpeta `resources`.

---

## 6. Resultados Obtenidos y Operaciones CRUD

Tras concluir las fases de desarrollo e integración, se obtuvieron excelentes resultados funcionales y técnicos, logrando un sistema que procesa datos tanto local como remotamente.

### 6.1. Rendimiento y Estructura Multiplataforma
- **Ejecución Nativa:** La aplicación se ejecuta de manera nativa como un archivo APK/AppBundle en **Android**, como una aplicación compilada nativamente en **iOS**, y en el navegador a través del motor optimizado de **WebAssembly (WasmJS)**.
- **Reutilización de Código:** Más del 90% del código fuente reside en el paquete `commonMain`. Únicamente se requirió código específico por plataforma para el arranque inicial de la aplicación y la inyección del driver nativo de la base de datos (SQLite vs WebWorker).

> 📷 **[Insertar captura de pantalla de la aplicación principal ejecutándose lado a lado en un emulador Android y en el navegador web aquí]**

### 6.2. Funcionamiento de las Operaciones CRUD (Offline-First)
El patrón *Offline-First* demostró que el flujo reactivo de Kotlin Flows refleja de manera inmediata en la interfaz gráfica cualquier cambio local. A continuación se evidencia el flujo completo de datos a través de las cuatro operaciones básicas (CRUD):

#### A. Crear (Create)
Al momento de registrar un nuevo `Customer` o `Product` desde el formulario, el usuario presiona el botón de guardar y la acción ocurre instantáneamente en la interfaz. Internamente, la aplicación intenta hacer el envío remoto (`POST`) a la API de Elysia.js; si tiene éxito, se consolida la información en la base de datos local SQLDelight. Si hay un error de red, el registro se guarda de todas formas en la base local para no interrumpir al usuario.
> 📷 **[Insertar captura de pantalla del Formulario de Creación con datos listos para guardar aquí]**
> 📷 **[Insertar captura de pantalla o fragmento de código de consola mostrando el éxito del guardado o el JSON del POST aquí]**

#### B. Leer / Listar (Read)
La visualización principal de la app consta de una lista (LazyColumn). Este listado **nunca** lee directamente de internet. Por diseño, siempre escucha los cambios de la base de datos local a través de `Flow`. Mientras el usuario navega viendo el listado, una corrutina silenciosa hace una petición al servidor (`GET`), y si detecta nuevos registros, simplemente los inserta en SQLDelight, ocasionando que la lista en pantalla se actualice por arte de magia sin interrumpir al usuario.
> 📷 **[Insertar captura de pantalla de la Lista de Clientes o Productos mostrándose en pantalla aquí]**
> 📷 **[Insertar captura de pantalla de la respuesta GET del servidor (Postman/Elysia) aquí]**

#### C. Actualizar (Update)
Al seleccionar un registro y editarlo, el sistema invoca la función de actualización en el repositorio. Ktor lanza un `PUT` hacia el backend y, una vez recibida la confirmación, se hace un `insertOrReplace` en SQLDelight, lo cual propaga el estado reactivo inmediatamente hasta el Compose UI.
> 📷 **[Insertar captura de pantalla editando un registro específico aquí]**
> 📷 **[Insertar captura de pantalla de la interfaz ya actualizada o confirmación del PUT aquí]**

#### D. Eliminar (Delete)
El proceso de borrado envía de manera optimista una solicitud `DELETE` mediante el servicio remoto y posteriormente borra el registro de la tabla local en SQLDelight. En pantalla, el elemento de la lista desaparece instantáneamente brindando una experiencia sumamente fluida.
> 📷 **[Insertar captura de pantalla del botón o acción de Eliminar un registro aquí]**
> 📷 **[Insertar captura de pantalla de la lista vacía o actualizada tras la eliminación aquí]**

---

## 7. Conclusión Final e Impacto del Proyecto

La finalización de este proyecto marca un hito importante en la adopción de tecnologías de desarrollo de software modernas. El ecosistema de **Kotlin Multiplatform (KMP)** demostró de manera contundente ser una alternativa superior frente a los enfoques tradicionales de desarrollo nativo separado y frente a otros frameworks híbridos.

**1. Aceleración del Tiempo de Desarrollo (Time-to-Market):**  
Históricamente, desarrollar para Android, iOS y Web implicaba mantener tres bases de código distintas en tres lenguajes diferentes (Kotlin, Swift y Javascript/TypeScript), además de asegurar tres lógicas de sincronización de base de datos independientes. En este proyecto, logramos centralizar la lógica de dominio, la persistencia de datos (SQLDelight), la conexión remota (Ktor), e incluso la Interfaz de Usuario (Compose Multiplatform) en un único repositorio. Esto no solo redujo drásticamente el tiempo de desarrollo inicial, sino que mitiga la aparición de bugs asimétricos entre plataformas.

**2. Modernización del Stack Tecnológico:**  
La migración desde librerías ancladas al ecosistema Android/JVM (como Retrofit, Gson y Room) hacia librerías verdaderamente multiplataforma (Ktor, Kotlinx.Serialization y SQLDelight) fue un éxito total. Estas herramientas probaron ser sumamente eficientes y seguras. El uso de **Koin** facilitó la abstracción y el desacoplamiento de dependencias de forma limpia, sin depender de anotaciones pesadas como lo hace Dagger/Hilt. 

**3. Arquitectura Robusta (Clean Architecture y Offline-First):**  
La división en capas lógicas garantizó un código altamente escalable. Más allá de la limpieza estructural, la implementación de la arquitectura **Offline-First** (apoyada en la reactividad de `Flow`) fue un salto cualitativo en la experiencia de usuario (UX). El hecho de que la aplicación "nunca espere" al servidor para dibujar la pantalla o guardar un registro, elimina los tiempos de carga en la vista del usuario, otorgando una sensación de velocidad nativa absoluta, independientemente de la calidad de la conexión a internet.

**4. El Salto a WebAssembly (WasmJS):**  
Uno de los logros técnicos más destacables de esta bitácora fue compilar satisfactoriamente el código de Kotlin a **WebAssembly**, permitiendo ejecutar una aplicación de grado móvil directamente en el navegador sin intermediarios de Javascript para la lógica core. Aunque requirió solucionar desafíos arquitectónicos complejos —como el manejo asíncrono de los Web Workers y los binarios `.wasm` para la base de datos SQL en el navegador—, el resultado final es una aplicación web que compite en rendimiento, suavidad a 60FPS y seguridad de memoria con aplicaciones de escritorio instalables.

En definitiva, esta bitácora confirma que construir software empresarial sobre Kotlin Multiplatform y Clean Architecture es actualmente el estándar óptimo. Permite entregar aplicaciones ricas, funcionales y reactivas a todas las plataformas del mercado sin sacrificar rendimiento nativo ni duplicar el costo de ingeniería.

---
*Fin de la bitácora*
