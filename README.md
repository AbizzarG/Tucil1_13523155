# Tugas Kecil 1 IF2211 Strategi Algoritma - Penyelesaian Puzzle IQ Puzzler Pro dengan Algoritma Brute Force

## Deskripsi Singkat
Program ini adalah sebuah aplikasi yang dapat menyelesaikan permainan puzzle IQ Puzzler Pro menggunakan algoritma Brute Force. IQ Puzzler Pro adalah permainan papan yang dimana pemain harus mengisi seluruh papan dengan potongan puzzle (piece) yang tersedia. Setiap potongan puzzle memiliki bentuk yang unik dan semua potongan harus digunakan untuk menyelesaikan puzzle.

## Fitur Program
- Antarmuka Grafis (GUI) untuk interaksi yang lebih mudah
- Visualisasi proses penyelesaian puzzle secara real-time
- Kemampuan menyimpan solusi dalam format teks (.txt) dan gambar (.png)
- Mendukung berbagai konfigurasi puzzle melalui file teks
- Penanganan rotasi dan pencerminan potongan puzzle
- Pencatatan statistik (waktu eksekusi dan jumlah iterasi)

## Struktur Program
```
.
├── src/                    # File-file source code
│   ├── model/             # Class-class model data
│   ├── solver/            # Algoritma penyelesaian puzzle
│   └── util/              # Class-class utilitas
├── bin/                   # File-file hasil kompilasi
├── doc/                   # Dokumentasi
├── test/                  # File test dan solusi
└── README.md
```

## Requirement Program
- Java Development Kit (JDK) 8 atau lebih tinggi
- Java Runtime Environment (JRE)
- Java Swing (sudah termasuk dalam JDK)

## Cara Kompilasi Program
1. Buka terminal atau command prompt
2. Arahkan ke direktori utama program
3. Jalankan perintah kompilasi:
```bash
javac -d bin src/*.java src/model/*.java src/solver/*.java src/util/*.java
```

## Cara Menjalankan Program
1. Setelah kompilasi berhasil, jalankan program dengan perintah:
```bash
java -cp bin App
```

## Cara Penggunaan Program
1. Klik tombol "Browse" untuk memilih file input puzzle (.txt)
2. Klik tombol "Solve" untuk mulai mencari solusi
3. Tunggu hingga proses pencarian selesai
4. Jika solusi ditemukan:
   - Hasil akan ditampilkan pada papan puzzle
   - Statistik waktu dan jumlah iterasi akan ditampilkan
   - Anda dapat menyimpan solusi dalam format .txt atau .png
5. Gunakan tombol "Reset" untuk mengatur ulang papan
6. Gunakan tombol "Stop" jika ingin menghentikan proses pencarian

## Format File Input
File input harus berformat .txt dengan struktur sebagai berikut:
```
N M P
S
puzzle_1_shape
puzzle_2_shape
...
puzzle_P_shape
```
Dimana:
- N M : Dimensi papan (N x M)
- P : Jumlah potongan puzzle
- S : Jenis kasus (DEFAULT/CUSTOM/PYRAMID) (Dalam program ini hanya di implementasikan jenis kasus DEFAULT)
- puzzle_shape : Bentuk potongan puzzle menggunakan huruf kapital (A-Z)

## Author
- Nama: M. Abizzar Gamadrian
- NIM: 13523155
- Kelas: K-03
- Program Studi: Teknik Informatika
- Institut Teknologi Bandung

## Catatan Tambahan
- Program dikembangkan sebagai bagian dari Tugas Kecil 1 mata kuliah IF2211 Strategi Algoritma
- Dokumentasi lebih lengkap dapat dilihat pada file Laporan di folder doc/
