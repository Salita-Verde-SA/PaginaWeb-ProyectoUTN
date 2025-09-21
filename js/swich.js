fetch('swich.html')
        .then(res => res.text())
        .then(data => {
          document.getElementById('swich').innerHTML = data;
        });