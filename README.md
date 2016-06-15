## cclib --- 一个刚刚开始的快速开发框架

### ccbox --- 一些通用工具类

### ccdao --- 数据库框架

    /**
     * Created by Creacc on 2016/6/15.
     */
    @CCTableEntity(name = "women_table", rule = CCTableEntity.INCLUDE)
    public class WomenEntity {
    
        @CCColumn
        private int id;
    
        @CCColumn
        private String name;
    
        /**
         * 由于table的rule为INCLUDE，所以不进行CCColumn标注的field将不会加入table中
         */
        private int age;
    }

    /**
     * CCTableEntity中rule默认为EXCLUDE
     *
     * Created by Creacc on 2016/6/15.
     */
    @CCTableEntity(name = "men_table")
    public class MenEntity {
    
        /**
         * CCKeyColumn中increment默认为false，表示key不自增
         */
        @CCKeyColumn(increment = true)
        private int id;
        
        /**
         * CCColumn的name默认会使用field名称
         */
        @CCColumn(name = "person_name")
        private String name;
        
        /**
         * 由于CCTableEntity的rule为EXCLUDE，所以即使不进行CCColumn标注，该field也会加入table
         */
        private double height;
        
        /**
         * 当CCTableEntity的rule为EXCLUDE时，进行CCTableExcludeColumn标注的field将不会加入table
         */
        @CCTableExcludeColumn
        private boolean isBadGuy;
    }
    
    public class PointResolver extends CCColumnResolver<Point> {
    
        /**
         * 默认构造器
         *
         * @param name 数据库列名
         */
        public PointResolver(String name) {
            super(name);
        }
    
        @Override
        public String columnType() {
            return "text";
        }
    
        @Override
        protected void innerSerialize(ContentValues values, String key, Point value) {
            values.put(key, new Gson().toJson(value));
        }
    
        @Override
        protected Point innerDeserialize(Cursor cursor, int index) {
            return new Gson().fromJson(cursor.getString(index), Point.class);
        }
    
        @Override
        public String getWhereArgument(Point value) {
            return new Gson().toJson(value);
        }
    }
    
    /**
     * CCDaoEntity的name和version均为选填
     * name默认采用table的名称
     * version默认为1
     *
     * Created by Creacc on 2016/6/15.
     */
    @CCDaoEntity(name = "self_dao", version = 2)
    @CCTableEntity(name = "self_table")
    public class SelfEntity {
    
        @CCKeyColumn
        private int id;
    
        private String name;
    
        @CCWhere(key = 1)
        private String address;
    
        @CCWhere(key = 2)
        private int year;
    
        @CCWhere(key = 2)
        private int month;
    
        @CCColumn(serializer = PointResolver.class)
        private Point position;
    
        public int getId() {
            return id;
        }
    
        public void setId(int id) {
            this.id = id;
        }
    
        public String getName() {
            return name;
        }
    
        public void setName(String name) {
            this.name = name;
        }
    
        public String getAddress() {
            return address;
        }
    
        public void setAddress(String address) {
            this.address = address;
        }
    
        public int getYear() {
            return year;
        }
    
        public void setYear(int year) {
            this.year = year;
        }
    
        public int getMonth() {
            return month;
        }
    
        public void setMonth(int month) {
            this.month = month;
        }
    
        public Point getPosition() {
            return position;
        }
    
        public void setPosition(Point position) {
            this.position = position;
        }
    }
    
    public void test(Context context) {

        String dir = Environment.getExternalStorageDirectory().getPath();

        // 创建dao，使用fromEntityClass的entity类一定要进行CCDaoEntity标注
        CCDao selfDao = CCDao.fromEntityClass(context, dir, SelfEntity.class);
        // 使用dao创建table
        CCTable<SelfEntity> selfTable = new CCTable<SelfEntity>(selfDao, SelfEntity.class);
        // 创建一个测试用的实体类
        SelfEntity self = new SelfEntity();
        self.setId(1);
        self.setName("自己");
        self.setAddress("地址");
        self.setYear(2000);
        self.setMonth(2);
        self.setPosition(new Point(100, 100));
        // 进行插入操作
        selfTable.insert(self);
        // 修改内容
        self.setMonth(6);
        // 进行更新操作，默认不传入key字段时，以主键为条件
        selfTable.update(self);
        // 再次修改内容
        self.setMonth(8);
        // 传入key为1，则根据address为条件进行更新
        selfTable.update(self, 1);
        // 再次修改内容
        self.setAddress("地址");
        // 传入key为2，则根据year和month为条件进行更新
        selfTable.update(self, 2);
        // 将数据删除，这里也可以传入key效果与update一样
        selfTable.delete(self);

        // 创建dao
        CCDao personDao = new CCDao(context, dir + "person_dao", 1);
        // 将两个table存入同一个dao
        CCTable<WomenEntity> womenTable = new CCTable<WomenEntity>(personDao, WomenEntity.class);
        CCTable<MenEntity> personTable = new CCTable<MenEntity>(personDao, MenEntity.class);
    }

### cclistview --- 针对listview的翻页控制器

### ccnio --- 便捷的网络操作

### ccdrag --- 简单的拖拽监听
