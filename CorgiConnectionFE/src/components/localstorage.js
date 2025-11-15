function localStorage() {
  let totaleBytes = 0;

  for (let chiave in localStorage) {
    if (localStorage.hasOwnProperty(chiave)) {
      const valore = localStorage.getItem(chiave);
      totaleBytes += chiave.length + valore.length;
    }
  }

  const maxBytes = 5 * 1024 * 1024; // 5MB
  const percentuale = ((totaleBytes / maxBytes) * 100).toFixed(2);

  console.log(`Spazio usato: ${(totaleBytes / 1024).toFixed(2)} KB`);
  console.log(`Percentuale usata: ${percentuale}%`);

  if (totaleBytes > maxBytes * 0.9) {
    alert("⚠️ Attenzione: stai per superare il limite di localStorage!");
  }
}

localStorage();
