package com.example.direction2;

import java.util.ArrayList;
import java.util.List;

public class ListVert {
    private static ListVert instance;
    private ListVert(){

    }

   public static ListVert getInstance(){
       if (instance == null) {
           instance = new ListVert();
       }
       return instance;
   }
   public List<Vert> getList(){
       List<Vert> listVert = new ArrayList<Vert>();

       Vert v1 = new Vert("Phòng chờ giảng 106", 21.03806, 105.783526);
       Vert v2 = new Vert("WC nam", 21.0380337, 105.783526);
       Vert v3 = new Vert("3", 21.0380337, 105.783461);
       Vert v4 = new Vert("Phòng học 107", 21.038, 105.783455);
       Vert v5 = new Vert("5", 21.038, 105.783425);
       Vert v6 = new Vert("6", 21.03798, 105.78338275);
       Vert v7 = new Vert("7", 21.038, 105.7833404);
       Vert v8 = new Vert("Phòng học 101", 21.0380015, 105.783298);
       Vert v9 = new Vert("9", 21.03804, 105.783292);
       Vert v10 = new Vert("10", 21.03806, 105.78338);
       Vert v11 = new Vert("WC nữ", 21.0380405, 105.783228);
       Vert v12 = new Vert("Phòng thí nghiệm 102", 21.038068, 105.783227);
       Vert v13 = new Vert("Phòng học 103", 21.0380955, 105.78323);
       Vert v14 = new Vert("14", 21.03807, 105.783273);
       Vert v15 = new Vert("15", 21.03809, 105.783273);
       Vert v16 = new Vert("Phòng trung tâm máy tính", 21.0380824, 105.783381);

       listVert.add(v1);
       listVert.add(v2);
       listVert.add(v3);
       listVert.add(v4);
       listVert.add(v5);
       listVert.add(v6);
       listVert.add(v7);
       listVert.add(v8);
       listVert.add(v9);
       listVert.add(v10);
       listVert.add(v11);
       listVert.add(v12);
       listVert.add(v13);
       listVert.add(v14);
       listVert.add(v15);
       listVert.add(v16);

       v1.addNeighbour(new Edge(37, v1, v3));
       v1.addNeighbour(new Edge(16, v1, v2));
       v2.addNeighbour(new Edge(16, v2, v1));
       v2.addNeighbour(new Edge(33, v2, v3));
       v3.addNeighbour(new Edge(37, v3, v1));
       v3.addNeighbour(new Edge(33, v3, v2));
       v3.addNeighbour(new Edge(33, v3, v5));
       v3.addNeighbour(new Edge(21, v3, v4));
       v3.addNeighbour(new Edge(43, v3, v10));
       v4.addNeighbour(new Edge(21, v4, v3));
       v4.addNeighbour(new Edge(35, v4, v6));
       v4.addNeighbour(new Edge(23, v4, v5));
       v5.addNeighbour(new Edge(23, v5, v4));
       v5.addNeighbour(new Edge(33, v5, v3));
       v5.addNeighbour(new Edge(14, v5, v6));
       v5.addNeighbour(new Edge(23, v5, v7));
       v6.addNeighbour(new Edge(35, v6, v4));
       v6.addNeighbour(new Edge(14, v6, v5));
       v6.addNeighbour(new Edge(15, v6, v7));
       v6.addNeighbour(new Edge(37, v6, v8));
       v7.addNeighbour(new Edge(23, v7, v8));
       v7.addNeighbour(new Edge(34, v7, v9));
       v7.addNeighbour(new Edge(23, v7, v5));
       v7.addNeighbour(new Edge(15, v7, v6));
       v8.addNeighbour(new Edge(37, v8, v6));
       v8.addNeighbour(new Edge(23, v8, v7));
       v8.addNeighbour(new Edge(22, v8, v9));
       v9.addNeighbour(new Edge(34, v9, v7));
       v9.addNeighbour(new Edge(22, v9, v8));
       v9.addNeighbour(new Edge(43, v9, v10));
       v9.addNeighbour(new Edge(33, v9, v11));
       v9.addNeighbour(new Edge(37, v9, v12));
       v9.addNeighbour(new Edge(26, v9, v14));
       v10.addNeighbour(new Edge(43, v10, v9));
       v10.addNeighbour(new Edge(43, v10, v3));
       v11.addNeighbour(new Edge(33, v11, v9));
       v11.addNeighbour(new Edge(27, v11, v14));
       v11.addNeighbour(new Edge(16, v11, v12));
       v12.addNeighbour(new Edge(37, v12, v9));
       v12.addNeighbour(new Edge(19, v12, v13));
       v12.addNeighbour(new Edge(23, v12, v14));
       v13.addNeighbour(new Edge(19, v13, v12));
       v14.addNeighbour(new Edge(26, v14, v9));
       v14.addNeighbour(new Edge(27, v14, v11));
       v14.addNeighbour(new Edge(23, v14, v12));
       v14.addNeighbour(new Edge(11, v14, v15));
       v15.addNeighbour(new Edge(11, v15, v14));
       v15.addNeighbour(new Edge(55, v15, v16));
       v16.addNeighbour(new Edge(55, v16, v15));

       return listVert;
   }

   public String[] getString(){
       return new String[]{"Phòng học 101", "Phòng thí nghiệm 102", "Phòng học 103",
               "Phòng thí nghiệm NVĐ 105", "Phòng chờ giảng 106", "Phòng học 107",
               "WC nam", "WC nữ", "Phòng Multimedia", "Phòng trung tâm máy tính",
               "Phòng máy chủ"};
   }
}
