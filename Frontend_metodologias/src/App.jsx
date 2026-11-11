import { useState, useEffect } from 'react';

function obterYouTubeId(url) {
  if (!url) return '';
  const regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|&v=)([^#&?]*).*/;
  const match = url.match(regExp);
  return (match && match[2].length === 11) ? match[2] : url;
}

export default function App() {
  const [filmes, setFilmes] = useState([]);
  const [filmeDetalhes, setFilmeDetalhes] = useState(null); // RF-08.3
  const [youtubeVideoUrl, setYoutubeVideoUrl] = useState(null); // RF-08.5

  useEffect(() => {
    fetch('http://localhost:8080/api/filmes')
      .then(res => res.json())
      .then(data => setFilmes(data))
      .catch(err => console.log("Erro ao carregar filmes:", err));
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
          <button className="btn btn-primary" onClick={() => setYoutubeVideoUrl(destaque.youtubeUrl)}>
            ▶ Assistir Trailer
          </button>
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

      {filmeDetalhes && (
        <div className="modal-bg" onClick={() => setFilmeDetalhes(null)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <button className="modal-close" onClick={() => setFilmeDetalhes(null)}>✕</button>
            <h2>{filmeDetalhes.titulo}</h2>
            <p style={{ margin: '10px 0', color: '#aaa', fontSize: '13px' }}>
              Ano: {filmeDetalhes.ano} | Gênero: {filmeDetalhes.categoria}
            </p>
            <p style={{ marginBottom: '15px', lineHeight: '1.4' }}>{filmeDetalhes.sinopse}</p>
            {filmeDetalhes.elenco && (
              <p style={{ fontSize: '13px', color: '#888', marginBottom: '15px' }}>
                <strong>Elenco:</strong> {filmeDetalhes.elenco}
              </p>
            )}
            <button className="btn btn-primary" onClick={() => { setYoutubeVideoUrl(filmeDetalhes.youtubeUrl); setFilmeDetalhes(null); }}>
              ▶ Ver Trailer
            </button>
          </div>
        </div>
      )}

      {youtubeVideoUrl && (
        <div className="modal-bg" onClick={() => setYoutubeVideoUrl(null)}>
          <div className="modal" style={{ maxWidth: '750px', background: '#000' }} onClick={e => e.stopPropagation()}>
            <button className="modal-close" onClick={() => setYoutubeVideoUrl(null)}>✕</button>
            <h3 style={{ marginBottom: '10px' }}>Reprodução do Trailer</h3>
            <div className="player-container">
              <iframe 
                src={`https://www.youtube.com/embed/${obterYouTubeId(youtubeVideoUrl)}?autoplay=1`} 
                title="Trailer YouTube" 
                allow="autoplay; encrypted-media" 
                allowFullScreen
              />
            </div>
          </div>
        </div>
      )}
    </div>
  );
}