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
- użyta biblioteka "Bouncy Castle" podobno super extra dla javy

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

- Aplikacja do robienia notatek - tutorial
https://www.youtube.com/watch?v=3b5xxb8w7lI
https://www.youtube.com/watch?v=0IIhK7cuM6A
https://www.youtube.com/watch?v=pa_lghjVQVA


- podpinanie bibliotek
    https://stackoverflow.com/questions/55761551/how-to-fix-error-failed-to-resolve-androidx-cardviewcardview1-0-0
- constraint layout
https://www.youtube.com/watch?v=4N4bCdyGcUc

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

# Napotkane problemy
- bcrypt nie porównuje haseł prawidłowo commit https://github.com/pawlaczyk/BDSM/commit/c522d9be75f68c81c6e82e231decca419ddff305
https://stackoverflow.com/questions/43513880/comparing-hashed-passwords-with-salt-bcrypt-always-returns-false

# problemy z bcryptem
- https://www.baeldung.com/java-password-hashing
"""5.3. Implementing BCrypt and SCrypt in Java
So, it turns out that BCrypt and SCrypt support don't yet ship with Java, though some Java libraries support them.
One of those libraries is Spring Security."""

# przechowywanie haseł użytkowników
https://nakedsecurity.sophos.com/2013/11/20/serious-security-how-to-store-your-users-passwords-safely/
https://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
https://crackstation.net/hashing-security.htm
https://cryptobook.nakov.com/crypto-libraries-for-developers/java-crypto-libraries

## PBKDF2WithHmacSHA256 ##
#Bouncy Castle
https://stackoverflow.com/questions/8397557/bouncycastle-on-android
You can also try the SpongyCastle library which is a precompiled build of BouncyCastle but tested with Android.

https://github.com/rtyley/spongycastle

# Przykład użycia Bouncy Castle
https://stackoverflow.com/questions/22580853/reliable-implementation-of-pbkdf2-hmac-sha256-for-java

# Czy to bezpieczne?
https://security.stackexchange.com/questions/179204/using-pbkdf2-for-hash-and-aes-key-generation-implementation

# fajne do api na kiedyś
https://medium.com/@rhamedy/encryption-decryption-of-data-based-on-users-password-using-pbkdf2-aes-algorithms-592f8c1bb79a

# //https://www.cryptoexamples.com/java_string_encryption_password_based_symmetric.html
tutaj problemy         SecureRandom random = SecureRandom.getInstanceStrong(); // wspierana od android 30 o.Ó

# próba z tą biblioteką
https://www.mindrot.org/projects/jBCrypt/
https://mvnrepository.com/artifact/org.mindrot/jbcrypt/0.4
https://stackoverflow.com/questions/49530139/how-to-add-the-jbcrypt-library-in-android-studio dodawanie biblioteki
 https://github.com/advisories/GHSA-gp32-7h29-rpxm
 
#Toolbar
    Manifest
            android:theme="@style/Theme.AppCompat.Light.NoActionBar">

    Layout
    <androidx.appcompat.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        android:background="@color/colorPrimary"/>
        
     Gradle
        implementation 'com.google.android.material:material:1.1.0'
        
      Java
       import androidx.appcompat.widget.Toolbar;

 
#Bouncy Castle
https://stackoverflow.com/questions/55187472/bouncycastle-connect-to-android-studio
implementation 'org.bouncycastle:bcpkix-jdk15on:1.56'


# ANDROID onCreateOptionsMenu not called
