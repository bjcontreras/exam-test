-- Opcional: crear esquema y usarlo
CREATE SCHEMA IF NOT EXISTS exam_service;
SET search_path = exam_service, public;

-- Tabla de exámenes (padre)
CREATE TABLE IF NOT EXISTS exam (
  id BIGSERIAL PRIMARY KEY,
  title TEXT NOT NULL,
  description TEXT,
  presentation_time TIMESTAMPTZ NOT NULL,
  created_at TIMESTAMPTZ DEFAULT now()
);

-- Preguntas (hija de exam)
CREATE TABLE IF NOT EXISTS question (
  id BIGSERIAL PRIMARY KEY,
  exam_id BIGINT NOT NULL REFERENCES exam(id) ON DELETE CASCADE,
  text TEXT NOT NULL,
  score NUMERIC(5,2) NOT NULL CHECK (score >= 0),
  position INT DEFAULT 0,
  created_at TIMESTAMPTZ DEFAULT now()
);

-- Opciones (hija de question)
CREATE TABLE IF NOT EXISTS option_choice (
  id BIGSERIAL PRIMARY KEY,
  question_id BIGINT NOT NULL REFERENCES question(id) ON DELETE CASCADE,
  text TEXT NOT NULL,
  is_correct BOOLEAN NOT NULL DEFAULT false,
  created_at TIMESTAMPTZ DEFAULT now()
);

-- Índice único parcial para asegurar una sola opción correcta por pregunta
CREATE UNIQUE INDEX IF NOT EXISTS ux_one_correct_option_per_question
  ON option_choice (question_id)
  WHERE is_correct = true;

-- Estudiantes
CREATE TABLE IF NOT EXISTS student (
  id BIGSERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  age INT,
  city TEXT,
  timezone TEXT NOT NULL,
  created_at TIMESTAMPTZ DEFAULT now()
);

-- Asignación de examen a estudiante
CREATE TABLE IF NOT EXISTS student_exam (
  id BIGSERIAL PRIMARY KEY,
  student_id BIGINT NOT NULL REFERENCES student(id) ON DELETE CASCADE,
  exam_id BIGINT NOT NULL REFERENCES exam(id) ON DELETE CASCADE,
  assigned_at TIMESTAMPTZ DEFAULT now(),
  presentation_local TIMESTAMP,
  presentation_tz TEXT,
  created_at TIMESTAMPTZ DEFAULT now()
);

-- Asegurar unicidad student-exam con índice (idempotente)
CREATE UNIQUE INDEX IF NOT EXISTS ux_student_exam_unique ON student_exam (student_id, exam_id);

-- Respuestas del estudiante
CREATE TABLE IF NOT EXISTS student_answer (
  id BIGSERIAL PRIMARY KEY,
  student_exam_id BIGINT NOT NULL REFERENCES student_exam(id) ON DELETE CASCADE,
  question_id BIGINT NOT NULL REFERENCES question(id) ON DELETE CASCADE,
  chosen_option_id BIGINT REFERENCES option_choice(id),
  answered_at TIMESTAMPTZ DEFAULT now()
);

-- Índice/constraint único para respuesta por pregunta (idempotente)
CREATE UNIQUE INDEX IF NOT EXISTS ux_answer_unique_per_question ON student_answer (student_exam_id, question_id);

-- (Opcional) Tabla de resultados
CREATE TABLE IF NOT EXISTS exam_result (
  id BIGSERIAL PRIMARY KEY,
  student_exam_id BIGINT NOT NULL UNIQUE REFERENCES student_exam(id) ON DELETE CASCADE,
  total_score NUMERIC(6,2) NOT NULL,
  graded_at TIMESTAMPTZ DEFAULT now()
);
