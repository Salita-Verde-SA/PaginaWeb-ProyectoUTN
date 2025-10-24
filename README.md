# Proyecto Final Programación UTN

Aplicación web completa desarrollada como trabajo integrador final de la materia Programación de la UTN.

## 📋 Tabla de Contenidos

- [Despliegue con Docker](#-despliegue-con-docker)
- [Despliegue con Podman](#-despliegue-con-podman)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Documentación Adicional](#-documentación-adicional)
- [Desarrollo Local](#-desarrollo-local)

## 🐳 Despliegue con Docker

### Requisitos Previos

- Docker instalado (versión 20.10 o superior)
- Docker Compose instalado (versión 1.29 o superior)

### Instalación de Docker

#### Windows

1. Descargar Docker Desktop desde [docker.com](https://www.docker.com/products/docker-desktop/)
2. Ejecutar el instalador y seguir las instrucciones
3. Reiniciar el sistema si es necesario
4. Verificar la instalación:

   ```powershell
   docker --version
   docker-compose --version
   ```

#### Linux (Genérico)

1. Actualizar el sistema:

   ```bash
   sudo apt-get update
   sudo apt-get upgrade -y
   ```

2. Instalar Docker:

   ```bash
   curl -fsSL https://get.docker.com -o get-docker.sh
   sudo sh get-docker.sh
   ```

3. Agregar usuario al grupo docker (opcional, evita usar sudo):

   ```bash
   sudo usermod -aG docker $USER
   newgrp docker
   ```

4. Instalar Docker Compose:

   ```bash
   sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
   sudo chmod +x /usr/local/bin/docker-compose
   ```

5. Verificar la instalación:

   ```bash
   docker --version
   docker-compose --version
   ```

### Despliegue del Proyecto

#### En Windows (PowerShell o CMD)

```powershell
# Clonar el repositorio
git clone <url-del-repositorio>
cd PaginaWeb-ProyectoUTN

# Construir y levantar los contenedores
docker-compose up -d --build

# Verificar que los contenedores estén corriendo
docker ps
```

#### En Linux

```bash
# Clonar el repositorio
git clone <url-del-repositorio>
cd PaginaWeb-ProyectoUTN

# Construir y levantar los contenedores
docker-compose up -d --build

# Verificar que los contenedores estén corriendo
docker ps
```

### Comandos Útiles

```bash
# Ver logs de los contenedores
docker-compose logs -f

# Detener los contenedores
docker-compose down

# Reiniciar los contenedores
docker-compose restart

# Reconstruir un servicio específico
docker-compose up -d --build <nombre-servicio>

# Eliminar contenedores, volúmenes e imágenes
docker-compose down -v --rmi all
```

## 🦭 Despliegue con Podman

Podman es una alternativa a Docker que no requiere un daemon ejecutándose y puede correr contenedores sin privilegios de root.

### Requisitos Previos

- Podman instalado (versión 3.0 o superior)
- Podman Compose instalado (opcional, recomendado)

### Instalación de Podman

#### Windows

1. Descargar Podman Desktop desde [podman.io](https://podman.io/getting-started/installation)
2. Ejecutar el instalador y seguir las instrucciones
3. Inicializar la máquina virtual de Podman:
   ```powershell
   podman machine init
   podman machine start
   ```

4. Verificar la instalación:
   ```powershell
   podman --version
   podman-compose --version
   ```

#### Linux (Genérico)

1. Instalar Podman (Debian/Ubuntu):
   ```bash
   sudo apt-get update
   sudo apt-get install -y podman
   ```

   Para Fedora/RHEL/CentOS:
   ```bash
   sudo dnf install -y podman
   ```

2. Instalar podman-compose:
   ```bash
   pip3 install podman-compose
   ```
   
   O usando el gestor de paquetes:
   ```bash
   sudo apt-get install -y podman-compose  # Debian/Ubuntu
   sudo dnf install -y podman-compose      # Fedora/RHEL
   ```

3. Verificar la instalación:
   ```bash
   podman --version
   podman-compose --version
   ```

### Despliegue del Proyecto con Podman

#### En Windows (PowerShell)

```powershell
# Clonar el repositorio
git clone <url-del-repositorio>
cd PaginaWeb-ProyectoUTN

# Construir y levantar los contenedores
podman-compose up -d --build

# Verificar que los contenedores estén corriendo
podman ps
```

#### En Linux

```bash
# Clonar el repositorio
git clone <url-del-repositorio>
cd PaginaWeb-ProyectoUTN

# Construir y levantar los contenedores
podman-compose up -d --build

# Verificar que los contenedores estén corriendo
podman ps
```

### Comandos Útiles de Podman

```bash
# Ver logs de los contenedores
podman-compose logs -f

# Detener los contenedores
podman-compose down

# Reiniciar los contenedores
podman-compose restart

# Reconstruir un servicio específico
podman-compose up -d --build <nombre-servicio>

# Eliminar contenedores, volúmenes e imágenes
podman-compose down -v
podman system prune -a --volumes

# Alias para compatibilidad con Docker (opcional)
alias docker=podman
alias docker-compose=podman-compose
```

### Diferencias entre Docker y Podman

- **Podman** no requiere un daemon en ejecución
- **Podman** puede ejecutar contenedores sin privilegios de root
- **Podman** es compatible con comandos de Docker (sintaxis similar)
- Los archivos `docker-compose.yml` funcionan con `podman-compose`

## 📁 Estructura del Proyecto

```
PaginaWeb-ProyectoUTN/
├── frontend/          # Aplicación cliente (HTML, CSS, JS)
├── backend/           # API y lógica de negocio
├── database/          # Scripts y configuración de BD(todavía no se crea)
├── docker-compose.yml # Configuración de contenedores
├── .env.example       # Variables de entorno de ejemplo
└── README.md          # Este archivo
```

Para más detalles sobre cada módulo, consulta los README específicos en cada directorio.

## 🛠️ Tecnologías Utilizadas

### Frontend

- **React** 18+ - Biblioteca de JavaScript para construir interfaces de usuario
- **React Router DOM** - Navegación y enrutamiento en la aplicación
- **Vite** - Build tool y servidor de desarrollo rápido
- HTML5
- CSS3
- JavaScript ES6+
- **Librerías de íconos:**
  - [Box Icons](https://boxicons.com/) v2.1.4
  - [Font Awesome](https://fontawesome.com/) v6.4.0
  - [Bootstrap Icons](https://icons.getbootstrap.com/) v1.13.1

### Backend

- **Java** 17+
- **Spring Boot** 3.x - Framework principal del backend
  - Spring Web - API REST
  - Spring Data MongoDB - Integración con MongoDB
  - Spring Validation - Validación de datos
- **Lombok** - Reducción de código boilerplate
- **MongoDB** - Base de datos NoSQL orientada a documentos
- **Maven** - Gestión de dependencias y construcción

### Base de Datos

- **MongoDB** 6.0+ - Base de datos NoSQL
  - Colecciones: `usuarios`, `publicaciones`, `publicadores`
  - Almacenamiento de imágenes en sistema de archivos

### DevOps

- **Docker** / **Podman** - Contenedorización
- **Docker Compose** / **Podman Compose** - Orquestación de contenedores
- Git - Control de versiones

## 📚 Documentación Adicional

- [Frontend README](./frontend/README.md) - Detalles sobre la interfaz de usuario
- [Backend README](./backend/README.md) - Documentación de la API y servicios
- [Database README](./database/README.md) - Esquema de base de datos y migraciones

## 💻 Desarrollo Local

### Sin Docker

*(Agregar instrucciones para ejecutar el proyecto localmente sin Docker)*

### Variables de Entorno

Copiar el archivo `.env.example` a `.env` y configurar las variables necesarias:

```bash
cp .env.example .env
```

## 👥 Autores

- *(Nombres de los integrantes del equipo)*

## 📄 Licencia

Este proyecto fue desarrollado como trabajo final de la materia de Programación III - Laboratorio para la UTN.

---

## Íconos Utilizados

```html
<!-- Box Icons -->
<link
  rel="stylesheet"
  href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css"
/>
<!-- Font Awesome -->
<link
  rel="stylesheet"
  href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
/>
<!-- Bootstrap Icons -->
<link
  rel="stylesheet"
  href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css"
/>
```
