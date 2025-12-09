CameraAppLeider – Aplicación de Cámara en Android

Aplicación móvil desarrollada con Kotlin y Jetpack Compose, que permite capturar fotos, almacenarlas, visualizarlas en una galería, navegar entre ellas en una vista detallada y eliminarlas cuando sea necesario.

El proyecto integra tecnologías modernas de Android como CameraX, Coil, Navigation Compose y Accompanist Permissions, siguiendo buenas prácticas de arquitectura, permisos, almacenamiento y diseño con Material Design 3.

*Características principales*

- Vista previa de la cámara en tiempo real usando CameraX

- Captura de fotografías en formato JPEG

- Guardado automático en almacenamiento privado de la app

- Galería en cuadrícula con carga eficiente (Coil + LazyVerticalGrid)

- Vista detallada con navegación horizontal (HorizontalPager)

- Eliminación de fotos con confirmación

- Manejo completo de permisos en tiempo de ejecución

- Navegación fluida con Navigation Compose

- Diseño moderno con Material 3

*Estructura del Proyecto*
app/src/main/java/com/lepineda/mycamera/
├── MainActivity.kt
├── CameraScreen.kt
├── GalleryScreen.kt
├── PhotoDetailDialog.kt
└── ui/
    └── theme/

*Requisitos del Sistema*

Hardware

- Dispositivo Android con cámara trasera

- 200 MB de espacio disponible mínimo

- Procesador ARMv7, ARMv8 o x86

Software

- Android 8.0 (API 26) mínimo

- Android 14 (API 35) recomendado

- Android Studio Hedgehog (2023.1.1) o superior

- JDK 11 o superior

🛠️ Instrucciones de Instalación y Configuración
1. Clonar el repositorio
git clone https://github.com/RedOrtiz/CameraAppLeider.git
cd MyCamera

2. Abrir en Android Studio

Abrir Android Studio

Seleccionar “Open Project”

Cargar la carpeta del repositorio

3. Sincronizar dependencias

Android Studio pedirá sincronizar el proyecto.
Presiona “Sync Now”.

4. Ejecutar la app

Conectar un dispositivo físico (recomendado)

O iniciar un emulador con cámara habilitada

Presionar Run (Shift + F10)

5. Otorgar permisos

La app solicitará:

Permiso de cámara

Debe otorgarse para usar la funcionalidad principal.

*Problemas Conocidos*

1. En dispositivos de gama baja (API 29), la galería puede tardar ~2 segundos al cargar más de 50 imágenes.

2. En algunos emuladores sin cámara física, la vista previa puede aparecer negra (limitación del AVD).

3. Si se deniega permanentemente el permiso de cámara, el usuario debe ir manualmente a Configuración → Aplicaciones para habilitarlo.

4. Las fotos se almacenan en el directorio privado, por lo que no aparecen en la galería del sistema.

*Tecnologías utilizadas*

 - Kotlin 1.9+

- Jetpack Compose (UI declarativa)

- CameraX (captura y preview)

- Coil (carga de imágenes)

- Navigation Compose

- Accompanist Permissions

- Material Design 3

*Autor*

Nombre: Leider Pineda Ortiz
Proyecto desarrollado para la asignatura:
Desarrollo de Aplicaciones Móviles
Evaluación de Proyecto Técnico (SO1, SO4, SO6, SO7)

*Licencia*

Este proyecto es de carácter académico.
Puede ser reutilizado y extendido para fines educativos o personales.
