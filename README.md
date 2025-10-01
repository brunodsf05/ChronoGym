# ![](/app.png) ChronoGym
![Capturas de la aplicación](/docs/hero.png)

[![Platform-Android](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)](https://www.android.com/)
[![Python](https://img.shields.io/badge/Made%20with-Python-3776AB?logo=python&logoColor=white)](https://www.python.org/)
[![GNU AGPL v3.0](https://img.shields.io/badge/license-AGPL--3.0-orange)]()

Tu aplicación para entrenar sin comerte la cabeza.

## ¿Qué es?
Es una aplicación pensada para que entrenes sin distracciones, sin contar y sin configurar relojes cada vez.
Tú solo te mueves, y ella te guía.
Perfecta para calistenia, gimnasio o rutinas personalizadas.

### Con ChronoGym puedes:

#### 🎧 Reproducir entrenamientos
Una guía 100% automática con voz y sonidos que te dice qué hacer y cuándo hacerlo. Como tener un entrenador personal que no cobra.

#### ✍️ Editar tus propias rutinas
Crea entrenamientos desde cero con sets dinámicos, isométricos y descansos. Control total, pero fácil.

#### 📤 Compartir rutinas
Exporta tus rutinas en JSON y pásalas a quien quieras.

#### 📥 Descargar rutinas desde el servidor
Explora una biblioteca online con filtros inteligentes y baja las que más te interesen.



## Sobre el proyecto
Este proyecto está dividido en dos componentes principales:

### Software

#### 📱 Cliente Android [(abrir carpeta)](/projects/android)
Desarrollado en Java con interfaz nativa (XML). Incluye editor visual, reproductor inteligente y sistema de importación/exportación.

#### 🌐 Servidor Python [(abrir carpeta)](/projects/server)
API REST construida con Flask. Devuelve rutinas etiquetadas para que puedas buscarlas por tipo de entrenamiento (calistenia, con equipo, por grupos musculares...).
