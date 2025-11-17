
/*
  PROYECTO: KidzBrain - Plataforma de aprendizaje gamificado para niños.
  Desarrollado por iMouse S.A. de C.V.
   © iMouse S.A. de C.V.
*/


/* Creación de la base de datos */
CREATE DATABASE IF NOT EXISTS kidzbrain_db;
USE kidzbrain_db;


/* Tabla `TBL_Grados` */

CREATE TABLE IF NOT EXISTS TBL_Grados (
  id_grado INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(45) NOT NULL,
  rango_edad VARCHAR(45) NOT NULL,
  PRIMARY KEY (id_grado)
);


/* Tabla `TBL_Usuarios` */

CREATE TABLE IF NOT EXISTS TBL_Usuarios (
  id_usuario INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  correo VARCHAR(100) NOT NULL UNIQUE,
  contraseña VARCHAR(255) NOT NULL,
  edad_hijo INT NOT NULL,
  fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_usuario)
);


/* Tabla `TBL_Materias` */

CREATE TABLE IF NOT EXISTS TBL_Materias (
  id_materia INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  PRIMARY KEY (id_materia)
);


/* Tabla `TBL_Lecciones` */

CREATE TABLE IF NOT EXISTS TBL_Lecciones (
  id_leccion INT NOT NULL AUTO_INCREMENT,
  id_materia INT NOT NULL,
  id_grado INT NOT NULL,
  titulo VARCHAR(150) NOT NULL,
  descripcion TEXT NULL,
  orden INT NOT NULL,
  PRIMARY KEY (id_leccion),
  FOREIGN KEY (id_materia) REFERENCES TBL_Materias (id_materia),
  FOREIGN KEY (id_grado) REFERENCES TBL_Grados (id_grado)
);


/* Tabla `TBL_Juegos` */
CREATE TABLE IF NOT EXISTS TBL_Juegos (
  id_juego INT NOT NULL AUTO_INCREMENT,
  id_leccion INT NOT NULL,
  nombre_juego VARCHAR(150) NOT NULL,
  tipo VARCHAR(45) NOT NULL,
  descripcion TEXT NULL,
  PRIMARY KEY (id_juego),
  FOREIGN KEY (id_leccion) REFERENCES TBL_Lecciones (id_leccion)
);


/* Tabla `TBL_Progreso`*/

CREATE TABLE IF NOT EXISTS TBL_Progreso (
  id_progreso INT NOT NULL AUTO_INCREMENT,
  id_usuario INT NOT NULL,
  id_juego INT NOT NULL,
  completado BOOLEAN NOT NULL,
  puntuacion_obtenida INT NULL,
  intentos_realizados INT NOT NULL DEFAULT 1,
  fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_progreso),
  FOREIGN KEY (id_usuario) REFERENCES TBL_Usuarios (id_usuario),
  FOREIGN KEY (id_juego) REFERENCES TBL_Juegos (id_juego)
);