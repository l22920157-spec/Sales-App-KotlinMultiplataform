# Bitácora académica del proyecto KMP1

**Proyecto:** KMP1 - Aplicación multiplataforma para gestión de productos y clientes  
**Tipo de proyecto:** Kotlin Multiplatform con interfaz compartida en Compose Multiplatform  
**Asignatura:** ______________________________  
**Estudiante:** ______________________________  
**Docente:** ______________________________  
**Institución:** ______________________________  
**Periodo académico:** ______________________________  
**Fecha de entrega:** ______________________________  

## 1. Resumen ejecutivo

El presente documento registra el proceso de análisis, desarrollo y organización del proyecto **KMP1**, una aplicación multiplataforma construida con **Kotlin Multiplatform** y **Compose Multiplatform**. El proyecto tiene como finalidad implementar una solución funcional para la administración de productos y clientes, integrando interfaz gráfica, navegación, consumo de servicios remotos, persistencia local y separación por capas.

Durante el desarrollo se observó una arquitectura orientada a buenas prácticas de software, con división entre capa de presentación, dominio, datos, red, almacenamiento local e inyección de dependencias. Esta organización permite que la aplicación sea mantenible, extensible y reutilizable en distintos entornos, principalmente Android, iOS, JavaScript y WebAssembly.

## 2. Datos generales del proyecto

| Elemento | Descripción |
| --- | --- |
| Nombre del proyecto | KMP1 |
| Módulo principal | `composeApp` |
| Aplicación iOS | `iosApp` |
| Lenguaje principal | Kotlin |
| Framework de interfaz | Compose Multiplatform |
| Gestión de dependencias | Gradle Kotlin DSL |
| Persistencia local | SQLDelight |
| Comunicación remota | Ktor Client |
| Inyección de dependencias | Koin |
| Patrón general | Arquitectura por capas con repositorios y casos de uso |

## 3. Objetivo general

Desarrollar una aplicación multiplataforma que permita gestionar información de productos y clientes mediante operaciones de consulta, registro, actualización, eliminación y búsqueda, utilizando una arquitectura organizada y tecnologías compatibles con varios sistemas operativos.

## 4. Objetivos específicos

- Configurar un proyecto Kotlin Multiplatform con soporte para Android, iOS, JavaScript y WebAssembly.
- Implementar una interfaz gráfica compartida mediante Compose Multiplatform.
- Organizar el código en capas de presentación, dominio y datos.
- Incorporar servicios remotos mediante Ktor Client para comunicación con una API REST.
- Implementar almacenamiento local con SQLDelight para productos y clientes.
- Aplicar inyección de dependencias con Koin para desacoplar componentes.
- Gestionar estados de pantalla mediante ViewModels, StateFlow y corrutinas.
- Implementar navegación entre pantallas de productos y clientes.

## 5. Descripción técnica del sistema

El proyecto se estructura alrededor del módulo `composeApp`, que contiene el código compartido de la aplicación. En `commonMain` se concentran los elementos reutilizables: modelos de dominio, repositorios, casos de uso, servicios remotos, fuentes de datos locales, pantallas, estados y ViewModels.

La aplicación presenta dos entidades principales:

- **Producto:** representado por código, descripción, categoría, precio, stock e indicador de impuesto.
- **Cliente:** representado por identificador, nombre, correo electrónico e historial de compras.

La navegación principal se define en `App.kt`, donde se establecen rutas para listar productos, listar clientes, crear productos, editar productos, crear clientes y editar clientes. Esta estructura permite un flujo claro entre los módulos funcionales de la aplicación.

## 6. Arquitectura observada

La arquitectura del proyecto se puede explicar en cinco niveles:

| Capa | Responsabilidad | Ejemplos en el proyecto |
| --- | --- | --- |
| Presentación | Pantallas, formularios, estados visuales y eventos de usuario | `ProductListScreen`, `CustomerListScreen`, `ProductViewModel`, `CustomerViewModel` |
| Dominio | Modelos, contratos de repositorio y casos de uso | `Product`, `Customer`, `CreateProductUseCase`, `ListCustomerUseCase` |
| Datos | Implementaciones concretas de repositorios y transformación de información | `ProductRepositoryImpl`, `CustomerRepositoryImpl`, mappers DTO-dominio |
| Red | Comunicación con API REST | `ProductApiService`, `CustomerApiService`, `HttpClienteFactory` |
| Persistencia local | Base de datos local y consultas SQL | `ProductEntity.sq`, `CustomerEntity.sq`, `DatabaseDriverFactory` |

Esta separación favorece la mantenibilidad, ya que cada módulo cumple una responsabilidad específica. Además, permite que la interfaz no dependa directamente de la API ni de la base de datos, sino de casos de uso y repositorios.

## 7. Tecnologías empleadas

| Tecnología | Uso dentro del proyecto |
| --- | --- |
| Kotlin Multiplatform | Compartir lógica entre Android, iOS, JS y WASM |
| Compose Multiplatform | Construcción de la interfaz gráfica compartida |
| Gradle Kotlin DSL | Configuración del proyecto, plugins y dependencias |
| Ktor Client | Consumo de endpoints REST para productos y clientes |
| Kotlinx Serialization | Serialización y deserialización de datos JSON |
| Koin | Inyección de dependencias en capas de red, repositorio, casos de uso y ViewModel |
| SQLDelight | Definición de tablas y consultas para persistencia local |
| Coroutines y Flow | Procesamiento asíncrono y emisión reactiva de estados |
| Navigation Compose | Navegación entre pantallas del sistema |

## 8. Bitácora de desarrollo

### Sesión 1. Exploración e inicialización del proyecto

Se revisó la estructura inicial del proyecto y se identificó que fue generado como una aplicación Kotlin Multiplatform. La carpeta `composeApp` fue reconocida como el núcleo compartido de la solución, mientras que `iosApp` funciona como punto de entrada para la aplicación iOS.

**Resultado:** se estableció una base multiplataforma adecuada para compartir interfaz y lógica de negocio.

### Sesión 2. Configuración de plataformas y dependencias

Se analizó la configuración de Gradle, observando soporte para Android, iOS, JavaScript y WebAssembly. En el archivo `composeApp/build.gradle.kts` se integraron dependencias relevantes como Compose, Ktor, Koin, SQLDelight, Navigation Compose, corrutinas y serialización.

**Resultado:** el proyecto quedó preparado para trabajar con múltiples plataformas y con una pila tecnológica moderna.

### Sesión 3. Modelado de dominio

Se definieron las entidades centrales del sistema: productos y clientes. Estas clases representan la información principal que será gestionada por la aplicación y funcionan como modelos independientes de la interfaz y de la persistencia.

**Resultado:** se estableció una base conceptual clara para las operaciones del sistema.

### Sesión 4. Diseño de la capa de datos

Se implementaron repositorios para productos y clientes. Los repositorios cumplen la función de coordinar la información proveniente del servicio remoto y de la base de datos local. En el caso de productos, se observa una estrategia en la que primero se intenta obtener información remota y luego se actualiza la fuente local.

**Resultado:** la aplicación cuenta con una capa de datos que permite desacoplar la lógica de negocio de los detalles técnicos de red y almacenamiento.

### Sesión 5. Integración con servicios remotos

Se incorporaron servicios API mediante Ktor Client. `ProductApiService` y `CustomerApiService` implementan operaciones de consulta, búsqueda, creación, actualización y eliminación a través de endpoints REST. La URL base se centraliza en `Constants.kt`, lo cual facilita ajustes futuros en la conexión con el backend.

**Resultado:** se habilitó la comunicación con un servidor externo para sincronizar productos y clientes.

### Sesión 6. Implementación de persistencia local

Se agregaron esquemas SQLDelight para las tablas `ProductEntity` y `CustomerEntity`. Cada esquema incluye operaciones básicas como seleccionar todos los registros, buscar por identificador, insertar o reemplazar y eliminar.

**Resultado:** el proyecto cuenta con persistencia local para conservar información y mejorar la disponibilidad de datos.

### Sesión 7. Construcción de casos de uso

Se organizaron acciones del sistema en casos de uso, como listar, crear, buscar, actualizar y eliminar productos o clientes. Esta decisión permite que la capa de presentación invoque acciones concretas sin conocer los detalles internos de los repositorios.

**Resultado:** se fortaleció la separación de responsabilidades y la claridad del flujo de negocio.

### Sesión 8. Manejo de estado con ViewModels

Se implementaron ViewModels para productos y clientes. Estos componentes utilizan `StateFlow`, `combine`, `stateIn` y corrutinas para construir estados de interfaz reactivos. También se incluyó búsqueda local por texto, permitiendo filtrar productos por descripción o código, y clientes por nombre o identificador.

**Resultado:** la interfaz puede reaccionar a cambios de datos y búsqueda de manera organizada.

### Sesión 9. Desarrollo de interfaz gráfica

Se construyeron pantallas para listar productos y clientes, con búsqueda, botones de navegación, botones flotantes de alta, tarjetas informativas, acciones de edición y diálogos de confirmación para eliminación. La navegación entre pantallas se centraliza en `App.kt`.

**Resultado:** la aplicación dispone de una experiencia de usuario funcional para consultar y administrar registros.

### Sesión 10. Revisión académica y oportunidades de mejora

Se identificaron puntos que pueden fortalecerse en versiones posteriores. Los validadores `ProductValidator` y `CustomerValidator` existen, pero aún no contienen reglas implementadas. También se observa que la URL base depende de una dirección IP local, por lo que podría parametrizarse según el ambiente de ejecución. Finalmente, convendría ampliar las pruebas automatizadas para cubrir repositorios, casos de uso, ViewModels y validaciones de formularios.

**Resultado:** se definieron mejoras técnicas viables para elevar la calidad del proyecto.

## 9. Evidencias técnicas del desarrollo

| Evidencia | Archivo o carpeta |
| --- | --- |
| Configuración del proyecto | `settings.gradle.kts`, `build.gradle.kts`, `composeApp/build.gradle.kts` |
| Catálogo de dependencias | `gradle/libs.versions.toml` |
| Navegación principal | `composeApp/src/commonMain/kotlin/edu/itvo/kmp1/App.kt` |
| Pantallas de productos | `ProductListScreen.kt`, `ProductFormScreen.kt` |
| Pantallas de clientes | `CustomerListScreen.kt`, `CustomerFormScreen.kt` |
| ViewModels | `ProductViewModel.kt`, `CustomerViewModel.kt`, `ProductFormViewModel.kt`, `CustomerFormViewModel.kt` |
| Servicios remotos | `ProductApiService.kt`, `CustomerApiService.kt` |
| Repositorios | `ProductRepositoryImpl.kt`, `CustomerRepositoryImpl.kt` |
| Persistencia local | `ProductEntity.sq`, `CustomerEntity.sq` |
| Inyección de dependencias | `Koin.kt`, `NetworkModule.kt`, `RepositoryModule.kt`, `UseCaseModule.kt`, `ViewModelModule.kt` |

## 10. Problemas detectados y soluciones aplicadas

| Problema o necesidad | Solución implementada |
| --- | --- |
| Compartir lógica entre varias plataformas | Uso de Kotlin Multiplatform y código común en `commonMain` |
| Crear una interfaz reutilizable | Implementación de Compose Multiplatform |
| Consumir datos externos | Integración de Ktor Client con servicios REST |
| Mantener datos disponibles localmente | Uso de SQLDelight como base de datos local |
| Reducir acoplamiento entre clases | Incorporación de Koin para inyección de dependencias |
| Organizar operaciones de negocio | Creación de casos de uso por entidad y acción |
| Manejar estados de carga y listas filtradas | Uso de ViewModels, StateFlow y corrutinas |

## 11. Resultados obtenidos

Como resultado del desarrollo, se obtuvo una aplicación multiplataforma con una estructura sólida para administrar productos y clientes. El sistema permite visualizar registros, realizar búsquedas, navegar entre módulos, acceder a formularios de creación y edición, eliminar registros mediante confirmación y sincronizar información con un backend REST.

Además, el uso de SQLDelight permite que la aplicación conserve una copia local de la información, lo que mejora la experiencia ante fallos de red o indisponibilidad temporal del servidor. La arquitectura implementada facilita la expansión futura del sistema, por ejemplo, agregando nuevas entidades, reglas de validación, autenticación, reportes o pruebas automatizadas.

## 12. Reflexión académica

El proyecto KMP1 representa un ejercicio práctico relevante para comprender el desarrollo de aplicaciones modernas con enfoque multiplataforma. A diferencia de una aplicación tradicional limitada a un solo sistema operativo, este proyecto aprovecha Kotlin Multiplatform para compartir lógica entre diferentes destinos, reduciendo duplicidad de código y favoreciendo la consistencia funcional.

Desde una perspectiva de ingeniería de software, el mayor aprendizaje se encuentra en la separación por capas. La existencia de modelos de dominio, repositorios, casos de uso, servicios remotos, fuentes locales y ViewModels permite entender cómo una aplicación puede crecer de forma ordenada. Esta estructura también facilita la evaluación, mantenimiento y prueba de cada componente.

## 13. Conclusiones

El proyecto cumple con una base funcional para la gestión de productos y clientes en un entorno multiplataforma. Su organización demuestra aplicación de conceptos importantes como arquitectura por capas, consumo de servicios REST, persistencia local, programación asíncrona, inyección de dependencias y manejo reactivo de estado.

La solución aún puede fortalecerse con reglas de validación completas, mayor cobertura de pruebas, configuración flexible de ambientes y mejoras visuales en formularios. Sin embargo, el estado actual del proyecto evidencia un avance significativo y coherente con un desarrollo académico universitario orientado a buenas prácticas.

## 14. Recomendaciones

- Implementar reglas en `ProductValidator` y `CustomerValidator`.
- Agregar pruebas unitarias para casos de uso, repositorios y ViewModels.
- Parametrizar la URL base para diferenciar ambientes de desarrollo, pruebas y producción.
- Documentar los endpoints esperados del backend REST.
- Revisar mensajes visibles al usuario para asegurar correcta codificación de caracteres.
- Incorporar manejo más detallado de errores de red y persistencia.
- Evaluar una estrategia formal de sincronización cuando existan cambios locales sin conexión.

## 15. Referencias consultadas dentro del proyecto

- `README.md`
- `settings.gradle.kts`
- `composeApp/build.gradle.kts`
- `gradle/libs.versions.toml`
- Archivos fuente ubicados en `composeApp/src/commonMain`
- Esquemas SQLDelight ubicados en `composeApp/src/commonMain/sqldelight`

