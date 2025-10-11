fetch('footer.html')
        .then(res => res.text())
        .then(data => {
          document.getElementById('footer').innerHTML = data;
        });

// FIXME Revisar si esto se queda o se va porque da error
// fetch('ModoOscuro.html')
//         .then(res => res.text())
//         .then(data => {
//           document.getElementById('modo-oscuro').innerHTML = data;
//         });



