package CategoryManegement;

import java.sql.ResultSet;
import java.util.ArrayList;

public class LoaiSanPham {
    public String MaLoai;  
    public String TenLoai; 
    
    public LoaiSanPham() 
    { 
    } 
    
    public LoaiSanPham(String ml, String tl) 
    { 
        MaLoai  = ml; 
        TenLoai = tl; 
    } 
    
    public LoaiSanPham(String tl) 
    { 
        TenLoai = tl; 
    } 
    
    public static ArrayList<LoaiSanPham> GetListLoaiSanPham() throws Exception  
    { 
        ArrayList<LoaiSanPham> list = new ArrayList();  
        String query = "select * from LoaiSP";  
        ResultSet rs = new SQLContext().ExcuteQuery(query);  
        while(rs.next()) 
        {  
            LoaiSanPham temp = new LoaiSanPham(); 
            temp.MaLoai = rs.getString("MaLoai");  
            temp.TenLoai = rs.getString("TenLoai");  
            list.add(temp); 
        } 
        return list;  
    }
    
    public static LoaiSanPham getOne (String maLoai) throws Exception
    {
        String query = String.format("select * from LoaiSP where Maloai = '%s'", maLoai);
        ResultSet rs = new SQLContext().ExcuteQuery(query);
        LoaiSanPham temp = new LoaiSanPham();
        if(rs.next())
        {
            temp.MaLoai = rs.getString("MaLoai");
            temp.TenLoai = rs.getString("TenLoai");
        }
        return temp;
    }
    
    @Override 
    public String toString() { 
        return this.MaLoai + " - " + this.TenLoai; 
    } 
}
