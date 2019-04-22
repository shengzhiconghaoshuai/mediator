/*渠道*/
CREATE TABLE CHANNEL(
    CHANNEL_ID INT(10) PRIMARY KEY AUTO_INCREMENT,  /*主键*/
    CODE VARCHAR(10) NOT NULL,              		/*代号*/
    NAME VARCHAR(20) NOT NULL,              		/*名称*/
    PRIORITY INT(10),                         		/*排序号*/
    STATUS INT(10),                          		/*状态*/
    CREATE_TIME DATETIME NOT NULL,          		/*创建时间*/
    UNIQUE KEY UK_CODE(CODE)
);
INSERT INTO CHANNEL(CODE,NAME,PRIORITY,STATUS,CREATE_TIME) VALUES ('TAOBAO','淘宝',0,1,CURRENT_TIMESTAMP);

/*应用*/
CREATE TABLE APPLICATION(
    APPLICATION_ID INT(10) PRIMARY KEY AUTO_INCREMENT,  /*主键*/
    NAME VARCHAR(20) NOT NULL,                          /*名称*/
    CODE VARCHAR(15) NOT NULL,				/*代号*/
    STORE_ID INT(10) NOT NULL,                          /* store id*/
    CHANNEL_ID INT(10) NOT NULL,                        /*渠道id*/
    CREATE_TIME DATETIME NOT NULL,                      /*创建时间*/
    APPKEY VARCHAR(50),
    APPSECRET VARCHAR(100),
    APPURL VARCHAR(200),
    SESSIONKEY VARCHAR(200),
    VENDOR_ID VARCHAR(20),
    VENDOR_NAME VARCHAR(20),
    NICK VARCHAR(100),
    PRIORITY INT(10),                                   /*排序号*/
    STATUS INT(10),                                     /*状态*/
    FIELD1 VARCHAR(200),                                /*预留扩展*/
    FIELD2 VARCHAR(100),                                /*预留扩展*/
    FIELD3 INT(10),                                     /*预留扩展*/
    FOREIGN KEY(CHANNEL_ID) REFERENCES CHANNEL(CHANNEL_ID),
    CONSTRAINT UK_CODE UNIQUE (CODE),
    CONSTRAINT UK_STORE_ID UNIQUE (STORE_ID)
);

/*task*/
CREATE TABLE TASK_TEMPLATE(
    TEMPLATE_ID INT(10) PRIMARY KEY AUTO_INCREMENT,      /*主键*/
    DESCRIPTION VARCHAR(50),                             /*描述*/
    TYPE VARCHAR(15) NOT NULL,                           /*类型*/
    SUBTYPE VARCHAR(30) NOT NULL,                        /*子类型*/
    REPEATABLE INT(1),                                   /*是否可重复*/
    RERUN INT(1),                                        /*是否可重跑*/
    HUNG INT(1),                                         /*是否允许挂起*/
    PRIORITY INT(10),                                    /*排序号*/
    STATUS INT(10),                                      /*状态*/
    UNIQUE KEY UK_CODE(TYPE,SUBTYPE)
);
CREATE TABLE TASK(
    TASK_ID INT(10) PRIMARY KEY AUTO_INCREMENT,                     /*主键*/
    TEMPLATE_ID INT(10),                                            /*模板id*/
    DATAID VARCHAR(50) NOT NULL,            
    `DATA` TEXT(7000),                                              /*json格式数据*/
    STARTTIME DATETIME NOT NULL,                                    /*开始时间*/
    ENDTIME DATETIME,                                               /*结束时间*/
    CHANNEL_ID INT(10) NOT NULL,                                    /*渠道编号*/
    APPLICATION_ID INT(10) NOT NULL,                                /*店铺*/
    STATUS INT(10) NOT NULL,                                        /*状态*/
    ERRORMESSAGE VARCHAR(150),                                      /*错误码*/
    FOREIGN KEY(CHANNEL_ID) REFERENCES CHANNEL(CHANNEL_ID),
    FOREIGN KEY(TEMPLATE_ID) REFERENCES TASK_TEMPLATE(TEMPLATE_ID),
    FOREIGN KEY(APPLICATION_ID) REFERENCES APPLICATION (APPLICATION_ID)
);

/*调度*/
CREATE TABLE CRON_CONFIG(
    CRON_ID INT(10) PRIMARY KEY AUTO_INCREMENT,               /*主键*/
    DESCRIPTION VARCHAR(50),                                  /*描述*/
    CLASS_NAME VARCHAR(200) NOT NULL,                         /*类名*/
    EXPRESSION VARCHAR(50) NOT NULL,                          /*cron表达式*/
    IS_GLOBAL INT(1) NOT NULL,                                /*是否全局*/
    STATUS INT(1) NOT NULL                                    /*状态*/
    --CHANNEL_ID INT(10) NOT NULL,                            /*渠道编号*/
    --APPLICATION_ID INT(10) NOT NULL                         /*店铺*/
    --FOREIGN KEY(CHANNEL_ID) REFERENCES CHANNEL(CHANNEL_ID),
    --FOREIGN KEY(APPLICATION_ID) REFERENCES APPLICATION (APPLICATION_ID)
);
CREATE TABLE CRON_PARAM(
    PARAM_ID INT(10) PRIMARY KEY AUTO_INCREMENT,    /*主键*/
    CRON_ID INT(10) NOT NULL,            
    PARAM_NAME VARCHAR(20) NOT NULL,                /*参数名*/
    PARAM_VALUE VARCHAR(50) NOT NULL,               /*参数值*/
    FOREIGN KEY(CRON_ID) REFERENCES CRON_CONFIG(CRON_ID) ON DELETE CASCADE
);
CREATE TABLE CRON_LASTTIME(
    APPLICATION_ID INT DEFAULT '0' NOT NULL,    --应用id
    TEMPLATE_ID INT DEFAULT '0' NOT NULL,       --模板id
    LASTTIME BIGINT DEFAULT '0',                --上次运行时间
    PRIMARY KEY (APPLICATION_ID, TEMPLATE_ID)
);