## cclib --- 一个刚刚开始的快速开发框架

### ccbox --- 一些通用工具类

### ccdao --- 数据库框架

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
