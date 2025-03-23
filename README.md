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

> Not: Geliştirme amacıyla demo içeriğini (örnek kullanıcılar, departmanlar) etkinleştirmek için `.env` dosyasında `DEMO_CONTENT_ENABLED=true` olarak ayarlayabilirsiniz.

### 📦 Servisler

Uygulama başlatıldığında aşağıdaki servisler kullanılabilir olacaktır:

* Spring Boot API: http://localhost:8080
* PostgreSQL: localhost:5432
* PgAdmin: http://localhost:5050
  * Email: admin@admin.com
  * Şifre: admin

### 👥 Varsayılan Kullanıcılar

Uygulama ilk kez başlatıldığında aşağıdaki departmanlar ve kullanıcılar otomatik olarak oluşturulacaktır:

> **Not**: Demo içeriği (örnek kullanıcılar ve departmanlar) oluşturma özelliği, yapılandırma dosyalarında `app.demo-content.enabled` parametresi ile kontrol edilmektedir. Bu özellik:
> - Dev ortamında (`application-dev.yaml`) varsayılan olarak etkindir
> - Prod ortamında (`application-prod.yaml`) varsayılan olarak devre dışıdır
> - `.env` dosyasında `DEMO_CONTENT_ENABLED` parametresi ile kontrol edilebilir

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

### Proje İşlemleri

#### Tüm Projeleri Listele

```http
GET /api/v1/projects
```

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Projects found",
  "status": 200,
  "data": [
    {
      "id": "uuid",
      "name": "Project Name",
      "description": "Project Description",
      "status": "ACTIVE"
    }
  ]
}
```

#### Proje Detaylarını Getir

```http
GET /api/v1/projects/{projectId}
```

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Project found",
  "status": 200,
  "data": {
    "id": "uuid",
    "name": "Project Name",
    "description": "Project Description",
    "status": "ACTIVE"
  }
}
```

#### Proje Durumunu Güncelle

```http
PATCH /api/v1/projects/{id}/status
```

**Yetki**: GROUP_MANAGER, PROJECT_MANAGER

#### Request
```json
{
  "status": "COMPLETED"
}
```

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Project state updated",
  "status": 200,
  "data": {
    "id": "uuid",
    "name": "Project Name",
    "description": "Project Description",
    "status": "COMPLETED"
  }
}
```

### Görev İşlemleri

#### Görev Oluştur

```http
POST /api/v1/tasks
```

**Yetki**: GROUP_MANAGER, PROJECT_MANAGER, TEAM_LEADER

#### Request
```json
{
  "title": "Task Title",
  "description": "Task Description",
  "priority": "HIGH",
  "projectId": "uuid",
  "assigneeId": "uuid"
}
```

#### Response (201 Created)
```json
{
  "success": true,
  "message": "Task created",
  "status": 201,
  "data": {
    "id": "uuid",
    "title": "Task Title",
    "description": "Task Description",
    "priority": "HIGH",
    "state": "TODO",
    "projectId": "uuid",
    "assigneeId": "uuid"
  }
}
```

#### Görev Detaylarını Getir

```http
GET /api/v1/tasks/{id}
```

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Task found",
  "status": 200,
  "data": {
    "id": "uuid",
    "title": "Task Title",
    "description": "Task Description",
    "priority": "HIGH",
    "state": "TODO",
    "projectId": "uuid",
    "assigneeId": "uuid"
  }
}
```

#### Tüm Görevleri Listele

```http
GET /api/v1/tasks
```

#### Response (200 OK)
```json
{
  "success": true,
  "message": "All tasks retrieved",
  "status": 200,
  "data": [
    {
      "id": "uuid",
      "title": "Task Title",
      "description": "Task Description",
      "priority": "HIGH",
      "state": "TODO",
      "projectId": "uuid",
      "assigneeId": "uuid"
    }
  ]
}
```

#### Projeye Göre Görevleri Listele

```http
GET /api/v1/tasks/project/{projectId}
```

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Project tasks retrieved",
  "status": 200,
  "data": [
    {
      "id": "uuid",
      "title": "Task Title",
      "description": "Task Description",
      "priority": "HIGH",
      "state": "TODO",
      "projectId": "uuid",
      "assigneeId": "uuid"
    }
  ]
}
```

#### Görev Durumuna Göre Listele

```http
GET /api/v1/tasks/state/{state}
```

#### Response (200 OK)
```json
{
  "success": true,
  "message": "State tasks retrieved",
  "status": 200,
  "data": [
    {
      "id": "uuid",
      "title": "Task Title",
      "description": "Task Description",
      "priority": "HIGH",
      "state": "TODO",
      "projectId": "uuid",
      "assigneeId": "uuid"
    }
  ]
}
```

#### Görev Önceliğine Göre Listele

```http
GET /api/v1/tasks/priority/{priority}
```

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Priority tasks retrieved",
  "status": 200,
  "data": [
    {
      "id": "uuid",
      "title": "Task Title",
      "description": "Task Description",
      "priority": "HIGH",
      "state": "TODO",
      "projectId": "uuid",
      "assigneeId": "uuid"
    }
  ]
}
```

#### Görevi Güncelle

```http
PATCH /api/v1/tasks/{id}
```

**Yetki**: GROUP_MANAGER, PROJECT_MANAGER, TEAM_LEADER

#### Request
```json
{
  "title": "Updated Task Title",
  "description": "Updated Task Description",
  "priority": "MEDIUM"
}
```

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Task updated",
  "status": 200,
  "data": {
    "id": "uuid",
    "title": "Updated Task Title",
    "description": "Updated Task Description",
    "priority": "MEDIUM",
    "state": "TODO",
    "projectId": "uuid",
    "assigneeId": "uuid"
  }
}
```

#### Görev Durumunu Güncelle

```http
PATCH /api/v1/tasks/{id}/state
```

#### Request
```json
{
  "status": "IN_PROGRESS",
  "reason": "Started working on the task"
}
```

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Task state updated",
  "status": 200,
  "data": {
    "id": "uuid",
    "title": "Task Title",
    "description": "Task Description",
    "priority": "HIGH",
    "state": "IN_PROGRESS",
    "projectId": "uuid",
    "assigneeId": "uuid"
  }
}
```

#### Görevi Kullanıcıya Ata

```http
PATCH /api/v1/tasks/{id}/assignee
```

**Yetki**: GROUP_MANAGER, PROJECT_MANAGER, TEAM_LEADER

#### Request
```json
{
  "assigneeId": "uuid"
}
```

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Task assignee updated",
  "status": 200,
  "data": {
    "id": "uuid",
    "title": "Task Title",
    "description": "Task Description",
    "priority": "HIGH",
    "state": "TODO",
    "projectId": "uuid",
    "assigneeId": "uuid"
  }
}
```

#### Görevi Sil

```http
DELETE /api/v1/tasks/{id}
```

**Yetki**: GROUP_MANAGER, PROJECT_MANAGER

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Task deleted",
  "status": 200,
  "data": null
}
```

### Yorum İşlemleri

#### Yorum Oluştur

```http
POST /api/v1/comments
```

#### Request
```json
{
  "content": "Comment content",
  "taskId": "uuid"
}
```

#### Response (201 Created)
```json
{
  "success": true,
  "message": "Comment created",
  "status": 201,
  "data": {
    "id": "uuid",
    "content": "Comment content",
    "taskId": "uuid",
    "createdAt": "2024-03-23T12:00:00Z"
  }
}
```

#### Göreve Göre Yorumları Listele

```http
GET /api/v1/comments/task/{taskId}
```

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Task comments retrieved",
  "status": 200,
  "data": [
    {
      "id": "uuid",
      "content": "Comment content",
      "taskId": "uuid",
      "createdAt": "2024-03-23T12:00:00Z"
    }
  ]
}
```

#### Yorum Detaylarını Getir

```http
GET /api/v1/comments/{id}
```

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Comment found",
  "status": 200,
  "data": {
    "id": "uuid",
    "content": "Comment content",
    "taskId": "uuid",
    "createdAt": "2024-03-23T12:00:00Z"
  }
}
```

#### Yorumu Sil

```http
DELETE /api/v1/comments/{id}
```

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Comment deleted",
  "status": 200,
  "data": null
}
```

### Ek İşlemleri

#### Dosya Yükle

```http
POST /api/v1/attachments/upload/{taskId}
```

#### Request
```
Content-Type: multipart/form-data
file: [dosya]
```

#### Response (201 Created)
```json
{
  "success": true,
  "message": "Attachment uploaded",
  "status": 201,
  "data": {
    "id": "uuid",
    "fileName": "example.pdf",
    "fileSize": 1024,
    "taskId": "uuid",
    "uploadedAt": "2024-03-23T12:00:00Z"
  }
}
```

#### Dosya İndir

```http
GET /api/v1/attachments/{id}
```

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Attachment retrieved",
  "status": 200,
  "data": {
    "id": "uuid",
    "fileName": "example.pdf",
    "fileSize": 1024,
    "taskId": "uuid",
    "uploadedAt": "2024-03-23T12:00:00Z"
  }
}
```

#### Göreve Göre Dosyaları Listele

```http
GET /api/v1/attachments/task/{taskId}
```

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Attachments retrieved",
  "status": 200,
  "data": [
    {
      "id": "uuid",
      "fileName": "example.pdf",
      "fileSize": 1024,
      "taskId": "uuid",
      "uploadedAt": "2024-03-23T12:00:00Z"
    }
  ]
}
```

#### Dosyayı Sil

```http
DELETE /api/v1/attachments/{id}
```

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Attachment deleted",
  "status": 200,
  "data": null
}
```

### Departman İşlemleri

#### Departman Bilgilerini Getir

```http
GET /api/v1/departments
```

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Department found",
  "status": 200,
  "data": {
    "id": "uuid",
    "name": "Software Development",
    "description": "Software Development Department"
  }
}
```

## 🔒 API Kullanımı

### Kimlik Doğrulama

#### Kayıt Ol

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

#### Giriş Yap

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

### Kullanıcı İşlemleri

#### Tüm Kullanıcıları Listele

```http
GET /api/v1/users
```

**Yetki**: GROUP_MANAGER, PROJECT_MANAGER

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Users found",
  "status": 200,
  "data": [
    {
      "id": 1,
      "username": "johndoe",
      "email": "john.doe@example.com",
      "role": "TEAM_MEMBER",
      "department": "Software Development"
    }
  ]
}
```

#### Kullanıcı Detaylarını Getir

```http
GET /api/v1/users/{username}
```

**Yetki**: GROUP_MANAGER, PROJECT_MANAGER, TEAM_LEADER veya kendi profilini görüntüleme

#### Response (200 OK)
```json
{
  "success": true,
  "message": "User found",
  "status": 200,
  "data": {
    "id": 1,
    "username": "johndoe",
    "email": "john.doe@example.com",
    "role": "TEAM_MEMBER",
    "department": "Software Development"
  }
}
```

#### Kullanıcıyı Departmana Ata

```http
PATCH /api/v1/users/{username}/department
```

**Yetki**: GROUP_MANAGER

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Department updated",
  "status": 200,
  "data": null
}
```

#### Kullanıcı Rolünü Güncelle

```http
PATCH /api/v1/users/{username}/role
```

**Yetki**: GROUP_MANAGER, PROJECT_MANAGER

#### Request
```json
{
  "role": "TEAM_LEADER"
}
```

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Role updated",
  "status": 200,
  "data": null
}
```

#### Kullanıcıyı Projeye Ata

```http
PATCH /api/v1/users/{username}/project
```

**Yetki**: GROUP_MANAGER, PROJECT_MANAGER

#### Request
```json
{
  "projectId": 1
}
```

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Project updated",
  "status": 200,
  "data": null
}
```

### Diğer İstekler

Diğer tüm API istekleri için JWT token'ı Authorization header'ında kullanılmalıdır:

```http
Authorization: Bearer your_jwt_token
```

### Proje İşlemleri

#### Tüm Projeleri Listele

```http
GET /api/v1/projects
```

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Projects found",
  "status": 200,
  "data": [
    {
      "id": "uuid",
      "name": "Project Name",
      "description": "Project Description",
      "status": "ACTIVE"
    }
  ]
}
```

#### Proje Detaylarını Getir

```http
GET /api/v1/projects/{projectId}
```

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Project found",
  "status": 200,
  "data": {
    "id": "uuid",
    "name": "Project Name",
    "description": "Project Description",
    "status": "ACTIVE"
  }
}
```

#### Proje Durumunu Güncelle

```http
PATCH /api/v1/projects/{id}/status
```

**Yetki**: GROUP_MANAGER, PROJECT_MANAGER

#### Request
```json
{
  "status": "COMPLETED"
}
```

#### Response (200 OK)
```json
{
  "success": true,
  "message": "Project state updated",
  "status": 200,
  "data": {
    "id": "uuid",
    "name": "Project Name",
    "description": "Project Description",
    "status": "COMPLETED"
  }
}
```

### Görev İşlemleri

// ... existing code ... 