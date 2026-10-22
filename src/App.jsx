import { useState, useEffect } from 'react';

export default function App() {
  const [filmes, setFilmes] = useState([]);

  useEffect(() => {
    fetch('http://localhost:8080/api/filmes')
      .then(res => res.json())
      .then(data => setFilmes(data))
      .catch(err => console.log("Aguardando Java", err));
  }, []);

  const categorias = [...new Set(filmes.map(f => f.categoria))];

  return (
    <div>
      <header className="header">
        <div className="logo">NETFLIX</div>
        <div className="nav-btns">
          <button className="active">Início</button>
        </div>
      </header>

      <main style={{ marginTop: '20px' }}>
        {categorias.map(cat => (
          <section key={cat} className="section">
            <h2 className="section-title">{cat}</h2>
            <div className="grid">
              {filmes.filter(f => f.categoria === cat).map(filme => (
                <div key={filme.id} className="card">
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