// Script de inicialización de MongoDB
// Este script se ejecuta automáticamente cuando se crea el contenedor por primera vez

const dbName = process.env.MONGO_INITDB_DATABASE || 'backend_db';
const username = process.env.MONGO_INITDB_ROOT_USERNAME || 'admin';
const password = process.env.MONGO_INITDB_ROOT_PASSWORD || 'admin123';

print('=================================');
print('Inicializando MongoDB');
print('=================================');

// Crear la base de datos y cambiar a ella
db = db.getSiblingDB(dbName);
print('✓ Base de datos "' + dbName + '" creada/seleccionada');

// Crear usuario para la base de datos con permisos de lectura/escritura
db.createUser({
    user: username,
    pwd: password,
    roles: [
        {
            role: 'readWrite',
            db: dbName
        }
    ]
});
print('✓ Usuario "' + username + '" creado con permisos readWrite en "' + dbName + '"');

// Crear las colecciones
const collections = ['usuarios', 'publicaciones', 'publicadores', 'pedidos'];

collections.forEach(collectionName => {
    db.createCollection(collectionName);
    print('✓ Colección "' + collectionName + '" creada');
});

// Crear índices para mejorar el rendimiento
print('\nCreando índices...');

// Índices para usuarios
db.usuarios.createIndex({ "email": 1 }, { unique: true });
db.usuarios.createIndex({ "username": 1 }, { unique: true });
db.usuarios.createIndex({ "dni": 1 }, { unique: true });
print('✓ Índices de usuarios creados');

// Índices para publicadores
db.publicadores.createIndex({ "email": 1 }, { unique: true });
db.publicadores.createIndex({ "username": 1 }, { unique: true });
db.publicadores.createIndex({ "cuit": 1 }, { unique: true });
print('✓ Índices de publicadores creados');

// Índices para publicaciones
db.publicaciones.createIndex({ "usuarioId": 1 });
db.publicaciones.createIndex({ "fechaCreacion": -1 });
db.publicaciones.createIndex({ "visible": 1 });
print('✓ Índices de publicaciones creados');

// Índices para pedidos
db.pedidos.createIndex({ "usuarioId": 1 });
db.pedidos.createIndex({ "publicadorId": 1 });
db.pedidos.createIndex({ "codigoConfirmacion": 1 }, { unique: true });
db.pedidos.createIndex({ "estado": 1 });
print('✓ Índices de pedidos creados');

// Insertar un documento dummy y eliminarlo para forzar la creación física de la BD
db.usuarios.insertOne({ _temp: true });
db.usuarios.deleteOne({ _temp: true });

print('\n=================================');
print('Base de datos "' + dbName + '" inicializada correctamente');
print('Colecciones creadas: ' + collections.length);
print('=================================\n');
