fetch('header.html')
        .then(res => res.text())
        .then(data => {
          document.getElementById('header').innerHTML = data;
          document.dispatchEvent(new Event('headerContentLoaded'));
        });