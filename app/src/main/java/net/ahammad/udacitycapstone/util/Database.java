package net.ahammad.udacitycapstone.util;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by alahammad on 9/5/15.
 */
public class Database {

    private static Database _instance;
    private static Realm realm;

    public Realm getRealm(){
        return realm;
    }

    public  void setRealm(Realm realm){
        this.realm = realm;
    }

    public static Database getInstance (Context context){
        if (_instance ==null){
            _instance = new Database();
            realm = Realm.getInstance(context);
        }
        return _instance;
    }

    public void insertReminder (ReminderBean bean){
        realm.beginTransaction();
        ReminderBean reminderBean = realm.copyToRealm(bean);
        realm.commitTransaction();
    }

    public RealmResults<ReminderBean> getReminders(){
        RealmResults<ReminderBean> list = realm.where(ReminderBean.class).findAll();
        return list;
    }


    public void deleteReminder (String title,String exDate){
        RealmQuery<ReminderBean> query = realm.where(ReminderBean.class);
        RealmResults<ReminderBean> data = query.equalTo("title",title).equalTo("exDate",exDate).findAll();
        realm.beginTransaction();
        data.remove(0);
        realm.commitTransaction();
    }
}
