package CategoryManegement;

import java.sql.ResultSet;
import java.util.ArrayList;

public class SanPham {
    public String MaSP;  
    public String TenSP;  
    public long DonGia;  
    public String MaLoai;  
     
    public String TenLoai; //vi yeu cau hien thi TenLoai khi load Jtable
    
    public SanPham() 
    {         
    } 
    
    public SanPham(String masp, String ten, long gia, String ml) 
    { 
        MaSP = masp; 
        TenSP = ten; 
        DonGia = gia; 
        MaLoai = ml; 
    }
    
    public static ArrayList<SanPham> GetListSanPham() throws Exception  
    { 
        ArrayList<SanPham> list = new ArrayList(); 
        String query = "select * from SanPham left join LoaiSP on SanPham.Maloai = LoaiSP.MaLoai";  
        ResultSet rs = new SQLContext().ExcuteQuery(query);  
        while(rs.next())
        {
            SanPham temp = new SanPham();
             
            temp.MaSP = rs.getString("MaSP");
            temp.TenSP = rs.getString("TenSP");
            temp.DonGia= Long.parseLong(rs.getString("DonGia"));
            temp.MaLoai = rs.getString("MaLoai");
            temp.TenLoai = rs.getString("TenLoai");
            list.add(temp);
        }
        return list;
    }
    
    public void Insert() throws Exception  
    { 
        String sql = String.format("INSERT INTO SanPham VALUES('%s', '%s', %s, '%s')", this.MaSP, this.TenSP, this.DonGia, this.MaLoai); 
        new SQLContext().ExcuteUpdate(sql); 
    }
    
    public void Update() throws Exception  
    { 
        String sql = String.format("UPDATE SanPham SET TenSP = '%s', DonGia = %s, MaLoai = '%s' WHERE MaSP = '%s'",  this.TenSP, this.DonGia, this.MaLoai,this.MaSP); 
        new SQLContext().ExcuteUpdate(sql); 
    }
    
    public static void Delete(String maSP) throws Exception  
    { 
        String sql = String.format("DELETE SanPham WHERE MaSP = '%s'", maSP); 
        new SQLContext().ExcuteUpdate(sql); 
    } 
}
