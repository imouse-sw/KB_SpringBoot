
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

CREATE TABLE IF NOT EXISTS tbl_progreso (
    id_progreso INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_leccion INT NOT NULL,  -- CAMBIO: Referencia directa a la Lección
    completado TINYINT(1) DEFAULT 0, -- 1 = Completada
    puntuacion_obtenida INT DEFAULT 0, -- Opcional: si la lección tiene un quiz final
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES tbl_usuarios(id_usuario),
    FOREIGN KEY (id_leccion) REFERENCES tbl_lecciones(id_leccion)
);

ALTER TABLE TBL_Usuarios
ADD foto_url VARCHAR(255) NULL;
