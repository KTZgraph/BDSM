# BDSM
Bezpieczeństwo Danych Systemów Mobilnych

Potenjcalne pytania i odpowiedzi:
# Ogólne
- Poprawna rejestracja (prezentacja)
- Niepoprawna rejestracja (prezentacja)
- Przechowywanie haseł w bazie (hashowanie)
# Logowanie
- Logowanie poprawne (prezentacja)
- Logowanie niepprawne (prezentacja)
- Porównywanie haseł użytkowniak podczas logowania
- bcrypt i próba ataku brute force na konto użytkownika
- Enumeracja użytkowników (czy da się przewidzieć nazwy użytkowników)
- Token Zabezpieczenia danych/widoku dla notatek tylko dla zalogowanych użytkowników
- wylogowywanie użytkownika
- zmiana hasła użytkownika
- Logi - nigdzie w logach nie przechowuję informacji o hasłach użytkownika; nawet przy niepoprawnym logowaniu
- SQLI https://en.wikipedia.org/wiki/SQL_injection mam babola w  :<  Cursor cursor = db.rawQuery("SELECT password FROM user WHERE username=?", new String[]{username});


# Notatki
- dodawanie nowej notatki
- edytowanie istniejacej notatki (wymagane potwierdzenie hasłem użytkownika)
- usuwanie istniejącej notatki (wymagane potwierdzenie hasłem użytkownika)

# Zabezpieczenia
- dlaczego bcrypt
- jak zabezpieczony jest widok
- jak zabezpieczone są operacja CRUD przed niezalogowanym użytkownikiem
- Czy po zdobyciu praw roota da się odczytać hasła użytkowników
- szyfrowanie bazy danych
- wymuszanie mocnych haseł na użytkowniku; walidacja hasła
- dodawanie danych do bazy - nie kleić na chama Stringa - zabezpieczenia przed SQLInjection

Bibliografia:
#Praktyka:

- Ogólny kurs androida
  https://www.udemy.com/course/android-od-komplentego-zera-do-zaangazowanego-developera/

- Tworzenie layoutów logowania i rejestracji (bez funckjonaności)
https://www.youtube.com/watch?v=GAdGmJxfcf8

- Logowanie rejestrajca bez szyfrowania
https://www.youtube.com/watch?v=1WPAXHhG6u0
https://www.youtube.com/watch?v=Jho1GC6cuVU

- Hashowanie haseł użytkownika
bcrypt
https://github.com/patrickfav/bcrypt
Ostatnia wersja biblioteki https://github.com/patrickfav/bcrypt/releases to 0.9.0

- Java tworzenie hashowanych haseł
https://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/#PBKDF2WithHmacSHA1

- Walidacja polityki haseł użytkownika
-> https://examples.javacodegeeks.com/core-java/util/regex/matcher/validate-password-with-java-regular-expression-example/
-> klasa UserPassword.java

- Wyciąganie konkretnego pola z bazy
https://www.programcreek.com/java-api-examples/?class=android.database.Cursor&method=getColumnIndexOrThrow
-> metoda login z klasy DatabaseHelper.java
-> https://stackoverrun.com/vi/q/2702505

PBKDF2
https://gist.github.com/scotttam/874426/e5a0e1317995e9388083eb455c5bb160ec2e1afd

# Teoria
- wykłady

- Ogólnie szyfry (w trakcie)
www.coursera.org/learn/crypto [Cryptography I]


Pentesty na kiedyś:
- Drozer https://github.com/FSecureLABS/drozer
- LuckyPatcher https://www.dobreprogramy.pl/Lucky-Patcher,Program,Android,79201.html
- GameGuardian https://gameguardian.net/download