package databaseoperations.softylogic.com.databaseoperations; /**
 * Created by Rehan on 8/6/2018.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelperForRecordings extends SQLiteOpenHelper{

    public DBHelperForRecordings(Context context, String name, CursorFactory factory,
                                 int version) {

        super(context, name, factory, version);
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table if not exists recDetails (id integer primary key autoincrement " +
                ", title text, lastModifiedDate text, duration text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS recDetails");
        onCreate(db);
    }

    public boolean insertdata(String title , String date , String duration)
    {
        SQLiteDatabase sdb=this.getWritableDatabase();
        sdb.execSQL("insert into recDetails (title, lastModifiedDate , duration) " +
                "values('"+title+"' , '"+date+"' ,'"+duration+"' )");
        return true;
    }

    public Cursor searchlvid(int id){
        SQLiteDatabase sdb= getReadableDatabase();
        Cursor c=sdb.rawQuery("select * from recDetails where lvid like '%"  + id + "%'", null);

            return c;
    }
    public boolean search(String title){
        SQLiteDatabase sdb= getReadableDatabase();
        Cursor c=sdb.rawQuery("select * from recDetails where title like '%"  + title + "%'", null);

        return c.getCount() > 0;
    }
    public Cursor getData()
    {
        SQLiteDatabase sdb= getReadableDatabase();
        Cursor c=sdb.rawQuery("select * from recDetails", null);
        return c;
    }
    public void deleteItem(String title)
    {

        SQLiteDatabase db=this.getWritableDatabase();
        db.delete("recDetails" ,"title = '" + title + "'",null);
        db.close();

    }

}
