fetch('footer.html')
        .then(res => res.text())
        .then(data => {
          document.getElementById('footer').innerHTML = data;
        });

fetch('ModoOscuro.html')
        .then(res => res.text())
        .then(data => {
          document.getElementById('modo-oscuro').innerHTML = data;
        });



