package com.landofoz.commonland.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.landofoz.commonland.domain.Floor;

import java.util.List;

public class FloorDAO extends GenericDAO {

	public final static String TABLE_NAME = "floor";
	public final static String COLUMN_NAME_NAME = "name";
	public final static String COLUMN_NAME_LEVEL = "level";

	Context context;

	private static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
	_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
	COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
	COLUMN_NAME_LEVEL + INTEGER_TYPE + COMMA_SEP +
	" )";

	public FloorDAO(Context context) {
		super(context, TABLE_NAME, SQL_CREATE);
		this.context = context;
	}

	private ContentValues getContentValues(Floor floor) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_NAME, floor.getName());
		values.put(COLUMN_NAME_LEVEL, floor.getLevel());
		return values;
	}

	public long insert(Floor floor) {
		return super.insert(getContentValues(floor));
	}

	public boolean update(Floor floor) {
		return super.update(floor.getLevel(), getContentValues(floor));
	}

	public Floor findFloorByID(long id) {
		String[] projection = {
			_ID,
			COLUMN_NAME_NAME,
			COLUMN_NAME_LEVEL,
		};

		String where = " "+_ID+" = ? ";
		
		String[] whereValues = {Long.toString(id)};

		String sortOrder = null;

		Cursor cursor = db.query(
			TABLE_NAME,
			projection,
			where,
			whereValues,
			null,
			null,
			sortOrder
		);

		List<Floor> floors = getFloors(cursor, context);
		return floors.size()==1?floors.get(0):null;
	}

}

