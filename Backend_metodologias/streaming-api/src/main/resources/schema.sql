
CREATE TABLE IF NOT EXISTS conteudos (
                                         id INT PRIMARY KEY,
                                         titulo VARCHAR(255) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    ano INT NOT NULL,
    genero VARCHAR(100) NOT NULL,
    sinopse CLOB,
    elenco CLOB,
    classificacao VARCHAR(50),
    imagem_url VARCHAR(1000),
    trailer_url VARCHAR(1000),
    destaque BOOLEAN DEFAULT FALSE
    );


CREATE TABLE IF NOT EXISTS usuarios (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL
    );


CREATE TABLE IF NOT EXISTS minha_lista (
                                           usuario_id INT NOT NULL,
                                           conteudo_id INT NOT NULL,

                                           PRIMARY KEY (usuario_id, conteudo_id),

    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (conteudo_id) REFERENCES conteudos(id)
    );

CREATE TABLE IF NOT EXISTS avaliacoes (
                                          id INT AUTO_INCREMENT PRIMARY KEY,
                                          usuario_id INT NOT NULL,
                                          conteudo_id INT NOT NULL,
                                          nota INT NOT NULL,
                                          data_avaliacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                          FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (conteudo_id) REFERENCES conteudos(id),
    UNIQUE (usuario_id, conteudo_id)
    );