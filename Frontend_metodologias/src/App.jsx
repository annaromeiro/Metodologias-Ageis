import { useState, useEffect } from 'react';

export default function App() {
  const [filmes, setFilmes] = useState([]);
  const [setFilmeDetalhes] = useState(null);

  useEffect(() => {
    fetch('http://localhost:8080/api/filmes')
      .then(res => res.json())
      .then(data => setFilmes(data))
      .catch(err => console.log("Erro ao carregar do Java:", err));
  }, []);

  const destaque = filmes[0];
  const categorias = [...new Set(filmes.map(f => f.categoria))];

  return (
    <div>
      <header className="header">
        <div className="logo">NETFLIX</div>
        <div className="nav-btns">
          <button className="active">Início</button>
        </div>
      </header>

      {destaque && (
        <section className="hero">
          <h1>{destaque.titulo}</h1>
          <p>{destaque.sinopse}</p>
          <button className="btn btn-secondary" onClick={() => setFilmeDetalhes(destaque)}>
            ⓘ Ficha Técnica
          </button>
        </section>
      )}

      <main>
        {categorias.map(cat => (
          <section key={cat} className="section">
            <h2 className="section-title">{cat}</h2>
            <div className="grid">
              {filmes.filter(f => f.categoria === cat).map(filme => (
                <div key={filme.id} className="card" onClick={() => setFilmeDetalhes(filme)}>
                  <img src={filme.imagemCapa} alt={filme.titulo} />
                  <div className="card-body">
                    <div className="card-title">{filme.titulo}</div>
                    <div className="card-sub">{filme.ano}</div>
                  </div>
                </div>
              ))}
            </div>
          </section>
        ))}
      </main>
      </div>
  );
}