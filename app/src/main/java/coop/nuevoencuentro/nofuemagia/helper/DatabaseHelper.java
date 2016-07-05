package coop.nuevoencuentro.nofuemagia.helper;

/**
 * Created by jlionti on 06/06/2016. No Fue Magia
 */
public class DatabaseHelper /*extends OrmLiteSqliteOpenHelper */{
/*
    private static final String DATABASE_NAME = "compras.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Caracteristica, Integer> caracteristicaDao;
    private Dao<EstadoPedido, Integer> estadoPedidoDao;
    private Dao<Pedido, Integer> pedidoDao;
    private Dao<Producto, Integer> productoDao;
    private Dao<ProductoCaracteristica, Integer> productoCaracteristicaDao;
    private Dao<Usuarios, Integer> usuarioDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Caracteristica.class);
            TableUtils.createTable(connectionSource, EstadoPedido.class);
            TableUtils.createTable(connectionSource, Pedido.class);
            TableUtils.createTable(connectionSource, Producto.class);
            TableUtils.createTable(connectionSource, ProductoCaracteristica.class);
            TableUtils.createTable(connectionSource, Usuarios.class);
        } catch (SQLException e) {
            System.err.println("Error al crear la base de datos");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        onCreate(database, connectionSource);
    }

    public Dao<Caracteristica, Integer> getCaracteristicaDao() throws SQLException {
        if (caracteristicaDao == null) {
            caracteristicaDao = getDao(Caracteristica.class);
        }
        return caracteristicaDao;
    }

    public Dao<EstadoPedido, Integer> getEstadoPedidoDao() throws SQLException {
        if (estadoPedidoDao == null) {
            estadoPedidoDao = getDao(EstadoPedido.class);
        }
        return estadoPedidoDao;
    }

    public Dao<Pedido, Integer> getPedidoDao() throws SQLException {
        if (pedidoDao == null) {
            pedidoDao = getDao(Pedido.class);
        }
        return pedidoDao;
    }

    public Dao<Producto, Integer> getProductoDao() throws SQLException {
        if (productoDao == null) {
            productoDao = getDao(Producto.class);
        }
        return productoDao;
    }

    public Dao<ProductoCaracteristica, Integer> getProductoCaracteristicaDao() throws SQLException {
        if (productoCaracteristicaDao == null) {
            productoCaracteristicaDao = getDao(ProductoCaracteristica.class);
        }
        return productoCaracteristicaDao;
    }

    public Dao<Usuarios, Integer> getUsuarioDao() throws SQLException {
        if (usuarioDao == null) {
            usuarioDao = getDao(Usuarios.class);
        }
        return usuarioDao;
    }

    @Override
    public void close() {
        super.close();
        caracteristicaDao = null;
        estadoPedidoDao = null;
        pedidoDao = null;
        productoDao = null;
        productoCaracteristicaDao = null;
        usuarioDao = null;
    }*/
}
