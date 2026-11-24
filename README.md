# Dompet Deadline

Dompet Deadline adalah aplikasi desktop berbasis Java yang dibuat untuk membantu mahasiswa mengatur keuangan dan mencatat tugas kuliah agar lebih terorganisir.

## Fitur Aplikasi

- Mencatat transaksi pemasukan dan pengeluaran.
- Menambahkan, mengedit, dan menghapus kategori transaksi.
- Menyimpan data menggunakan database SQLite.
- Mengelola tugas kuliah dan deadline.
- Menampilkan ringkasan keuangan dan tugas.

## Teknologi yang Digunakan

- Java
- Java Swing
- JDBC
- SQLite

## Struktur Folder

```text
DompetDeadline/
├── lib/
│   └── sqlite-jdbc-3.54.1.0.jar
├── src/
│   ├── Main.java
│   ├── model/
│   ├── repository/
│   └── view/
├── .gitignore
└── README.md
```

## Cara Menjalankan

1. Pastikan Java JDK sudah terinstal.
2. Clone repository Dompet Deadline.
3. Buka folder project menggunakan Visual Studio Code.
4. Pastikan library SQLite JDBC tersedia di folder `lib`.
5. Compile dan jalankan file `Main.java` dengan library