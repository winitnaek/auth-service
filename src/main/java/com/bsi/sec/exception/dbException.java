package com.bsi.sec.exception;

import java.sql.SQLException;

//import com.bsi.sec.tpfdao.dbAccess;

public class dbException  extends SQLException
{
	private static final long serialVersionUID = 1L;

	public dbException (SQLException ex)
    {
        super(ex.getMessage (),ex.getSQLState (),ex.getErrorCode ());
    }
    
    /*public int GetBSICode(){
        return dbAccess.GetMappedErrorCode (getErrorCode());
    }*/
}