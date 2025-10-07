-- Tabla de exámenes
CREATE TABLE exam (
  id BIGSERIAL PRIMARY KEY,
  title TEXT NOT NULL,
  description TEXT,
  presentation_time TIMESTAMPTZ NOT NULL, -- momento global del examen (p. ej. dado en America/Bogota y convertido)
  created_at TIMESTAMPTZ DEFAULT now()
);

-- Preguntas
CREATE TABLE question (
  id BIGSERIAL PRIMARY KEY,
  exam_id BIGINT NOT NULL REFERENCES exam(id) ON DELETE CASCADE,
  text TEXT NOT NULL,
  score NUMERIC(5,2) NOT NULL CHECK (score >= 0),
  position INT DEFAULT 0, -- orden de la pregunta
  created_at TIMESTAMPTZ DEFAULT now()
);

-- Opciones (4 por pregunta)
CREATE TABLE option_choice (
  id BIGSERIAL PRIMARY KEY,
  question_id BIGINT NOT NULL REFERENCES question(id) ON DELETE CASCADE,
  text TEXT NOT NULL,
  is_correct BOOLEAN NOT NULL DEFAULT false,
  created_at TIMESTAMPTZ DEFAULT now()
);

-- Asegura una sola opción correcta por pregunta
CREATE UNIQUE INDEX ux_one_correct_option_per_question ON option_choice (question_id)
  WHERE is_correct = true;

-- Estudiantes
CREATE TABLE student (
  id BIGSERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  age INT,
  city TEXT,
  timezone TEXT NOT NULL, -- almacenar nombre de zona IANA, ej. 'America/Bogota'
  created_at TIMESTAMPTZ DEFAULT now()
);

-- Asignación de examen a estudiante
CREATE TABLE student_exam (
  id BIGSERIAL PRIMARY KEY,
  student_id BIGINT NOT NULL REFERENCES student(id) ON DELETE CASCADE,
  exam_id BIGINT NOT NULL REFERENCES exam(id) ON DELETE CASCADE,
  assigned_at TIMESTAMPTZ DEFAULT now(), -- cuando se asignó
  -- NOTA: no es necesario almacenar un campo "presentation_in_student_tz",
  -- se convierte al vuelo en la API. Si desea conservar el "local time" puede añadirse:
  presentation_local TIMESTAMP, -- opcional: hora local del estudiante
  presentation_tz TEXT, -- opcional: la zona horaria del estudiante copiada aquí
  UNIQUE(student_id, exam_id)
);

-- Respuestas del estudiante
CREATE TABLE student_answer (
  id BIGSERIAL PRIMARY KEY,
  student_exam_id BIGINT NOT NULL REFERENCES student_exam(id) ON DELETE CASCADE,
  question_id BIGINT NOT NULL REFERENCES question(id) ON DELETE CASCADE,
  chosen_option_id BIGINT REFERENCES option_choice(id),
  answered_at TIMESTAMPTZ DEFAULT now(),
  CONSTRAINT uq_answer_unique_per_question UNIQUE (student_exam_id, question_id)
);