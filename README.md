# Task Management API

Bu proje, görev yönetimi için geliştirilmiş bir REST API'dir. Spring Boot, PostgreSQL ve JWT authentication kullanılarak geliştirilmiştir.

## 🚀 Başlangıç

Bu talimatlar, projeyi local ortamınızda geliştirme ve test amacıyla çalıştırmanız için gereken adımları içerir.

### 📋 Ön Gereksinimler

* Java 21
* Docker ve Docker Compose
* Maven

### 🔧 Kurulum

1. Projeyi klonlayın
```bash
git clone https://github.com/tarikkamat/TaskManagement.git
cd TaskManagement
```

2. Environment dosyasını oluşturun
```bash
cp example.env .env
```
> Not: Güvenlik için `.env` dosyasındaki değerleri production ortamında değiştirmeyi unutmayın.

3. Maven ile projeyi derleyin
```bash
./mvnw clean package -DskipTests
```

4. Docker containerlarını başlatın
```bash
docker-compose -f docker/docker-compose.yaml up -d
```

### 📦 Servisler

Uygulama başlatıldığında aşağıdaki servisler kullanılabilir olacaktır:

* Spring Boot API: http://localhost:8080
* PostgreSQL: localhost:5432
* PgAdmin: http://localhost:5050
  * Email: admin@admin.com
  * Şifre: admin

### 👥 Varsayılan Kullanıcılar

Uygulama ilk kez başlatıldığında aşağıdaki departmanlar ve kullanıcılar otomatik olarak oluşturulacaktır:

#### Departmanlar
- Software Development
- Marketing

#### Örnek Kullanıcılar (Rol Bazlı)

1. Proje Yöneticisi (PROJECT_MANAGER)
   * Ad: Software Project Manager
   * Email: project.manager@example.com
   * Kullanıcı Adı: projectmanager
   * Şifre: Manager123!
   * Departman: Software Development

2. Grup Yöneticisi (GROUP_MANAGER)
   * Ad: Software Manager
   * Email: software.manager@example.com
   * Kullanıcı Adı: softwaremanager
   * Şifre: Manager123!
   * Departman: Software Development

3. Takım Lideri (TEAM_LEADER)
   * Ad: Backend Team Leader
   * Email: backend.lead@example.com
   * Kullanıcı Adı: backendlead
   * Şifre: Leader123!
   * Departman: Software Development

4. Takım Üyesi (TEAM_MEMBER)
   * Ad: Backend Developer 1
   * Email: backend1@example.com
   * Kullanıcı Adı: backend1
   * Şifre: Member123!
   * Departman: Software Development

> Not: Bu kullanıcılar sadece veritabanı boş olduğunda oluşturulur. Eğer veritabanında kullanıcı varsa, yeni kullanıcılar oluşturulmaz.

## 🔒 API Kullanımı

### Kayıt Ol

```http
POST /api/v1/register
```

| Parametre | Tip     | Açıklama                |
| :-------- | :------- | :------------------------- |
| `username` | `string` | **Gerekli**. Kullanıcı adı (3-50 karakter) |
| `email` | `string` | **Gerekli**. Geçerli bir email adresi |
| `password` | `string` | **Gerekli**. Şifre (en az 6 karakter) |

#### Request
```json
{
  "username": "johndoe",
  "email": "john.doe@example.com",
  "password": "JohnDoe123!"
}
```

#### Response (201 Created)
```json
{
  "success": true,
  "message": "Registration successful",
  "status": 201,
  "data": null
}
```

### Giriş Yap

```http
POST /api/v1/authenticate
```

| Parametre | Tip     | Açıklama                |
| :-------- | :------- | :------------------------- |
| `identifier` | `string` | **Gerekli**. Email veya kullanıcı adı |
| `password` | `string` | **Gerekli**. Şifre |

#### Request
```json
{
  "identifier": "john.doe@example.com",
  "password": "JohnDoe123!"
}
```

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Login successful",
  "status": 200,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "expirationIn": 3600000
  }
}
```

### Diğer İstekler

Diğer tüm API istekleri için JWT token'ı Authorization header'ında kullanılmalıdır:

```http
Authorization: Bearer your_jwt_token
```

## 🧪 Test

### Test Çalıştırma

Projeyi test etmek için aşağıdaki komutları kullanabilirsiniz:

```bash
# Tüm testleri çalıştır
./mvnw test

# Belirli bir test sınıfını çalıştır
./mvnw test -Dtest=UserServiceTest

# Belirli bir test metodunu çalıştır
./mvnw test -Dtest=UserServiceTest#testCreateUser

# Test coverage raporu oluştur
./mvnw verify
```

### Test Coverage Raporu

Test coverage raporu oluşturulduktan sonra `target/site/jacoco/index.html` dosyasında bulunabilir.

> Not: Test coverage raporunu görüntülemek için bir web tarayıcısı kullanabilirsiniz.

## 🛠️ Teknolojiler

* Spring Boot 3
* Spring Security
* PostgreSQL
* JWT
* Docker
* Maven 