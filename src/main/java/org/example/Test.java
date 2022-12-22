package org.example;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import org.locationtech.jts.algorithm.distance.DistanceToPoint;
import org.locationtech.jts.algorithm.distance.PointPairDistance;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import static org.example.GeoTool.*;

/**
 * @description:
 * @author：kun.wang
 * @date: 2022/12/22
 * @assert: 自律自律自律
 */
public class Test {

    public static void main(String[] args) throws FactoryException, TransformException, ParseException {

        ////// 注意: 这里 规定为 x - lng , y - lat, 暂时无高度

        GeometryFactory geometryFactory = new GeometryFactory();

        /////////// 案例1 - 多边形与线
        // 多边形 和 线 是否相交
        Polygon polygon1 = geometryFactory.createPolygon(new Coordinate[]{
                getC(42.14448468776463, 100.07954341693423 ),
                getC(41.80213618483701, 100.80381378340472 ),
                getC(41.37788480931623, 100.03909912055236 ),
                getC(42.14448468776463, 100.07954341693423 ),
        });

        // 不相交
        LineString line1 = geometryFactory.createLineString(new Coordinate[]{
                getC(42.190187412421366, 99.65009246481608 ),
                getC(41.180791151623225, 100.02217794346933 )
        });

        // 相交
        LineString line2 = geometryFactory.createLineString(new Coordinate[]{
                getC(41.18498619696706, 100.02218159189148 ),
                getC(42.10107240790116, 100.56489757229099 )
        });

        TimeInterval timer = DateUtil.timer();

        for (int i = 9999; i > 0; i--) {
            boolean result = intersectsLineAndPolygon(line1, polygon1);
        }
        System.out.println(timer.intervalMs());

        // 相交
        LineString line3 = geometryFactory.createLineString(new Coordinate[]{
                getC(41.79957413170533, 100.55085811859742 ),
                getC(42.10107240790116, 100.56489757229099 )
        });

        System.out.println("预期不相交:" + intersectsLineAndPolygon(line1, polygon1));
        System.out.println("预期相交:" + intersectsLineAndPolygon(line2, polygon1));

        System.out.println("预期相交:" + intersectsLineAndPolygon(line3, polygon1));


        ////////案例2 圆与线
        Circle circle = new Circle();

        circle.setLng(99.68308664635059);
        circle.setLat(41.76297097622921);
        circle.setRadius(44660);

        LineString line4 = geometryFactory.createLineString(new Coordinate[]{
                getC(41.64461143872334, 99.77165984896688),
                getC(41.49394763168085, 99.99494250495145)
        });
        LineString line5 = geometryFactory.createLineString(new Coordinate[]{
                getC(41.64461143872334, 99.77165984896688),
                getC(42.42290344406264, 99.32221849371496)
        });
        LineString line6 = geometryFactory.createLineString(new Coordinate[]{
                getC(42.42290344406264, 99.32221849371496),
                getC(41.49394763168085, 99.99494250495145)
        });
        LineString line7 = geometryFactory.createLineString(new Coordinate[]{
                getC(42.42580886227416, 99.31411460441588),
                getC(41.66371154339852, 100.28037558988893)
        });
        LineString line8 = geometryFactory.createLineString(new Coordinate[]{
                getC(41.669653280874115, 100.28832416953553),
                getC(42.44142072433485, 99.41055280330188)
        });
        LineString line9 = geometryFactory.createLineString(new Coordinate[]{
                getC(41.84915348720384, 99.16769093707519),
                getC(41.620820569461955, 99.1522223195217)
        });

        System.out.println("预期相交:" + intersectsLineAndCircle(line4, circle));
        System.out.println("预期相交:" + intersectsLineAndCircle(line5, circle));
        System.out.println("预期相交:" + intersectsLineAndCircle(line6, circle));
        System.out.println("预期相交:" + intersectsLineAndCircle(line7, circle));
        System.out.println("预期不相交:" + intersectsLineAndCircle(line9, circle));


        ///// 案例3
        Circle circle2 = new Circle();

        circle2.setLng(109.49);
        circle2.setLat(35.48);
        circle2.setRadius(19952);


        LineString line31 = geometryFactory.createLineString(new Coordinate[]{
                get(109.46, 35.68),
                get(109.67, 35.67)
        });

        LineString line32 = geometryFactory.createLineString(new Coordinate[]{
                get(109.46, 35.68),
                get(109.58, 35.59)
        });
        LineString line33 = geometryFactory.createLineString(new Coordinate[]{
                get(109.37, 35.67),
                get(109.57, 35.23)
        });
        System.out.println(distanceForPointToPoint(geometryFactory.createPoint(new Coordinate(109.49, 35.48)),
                geometryFactory.createPoint(new Coordinate(109.71, 35.47))));
        System.out.println(intersectsLineAndCircle(line31, circle2));
//
//        System.out.println();
        System.out.println(intersectsLineAndCircle(line32, circle2));
        System.out.println(intersectsLineAndCircle(line33, circle2));

    }

    public static Coordinate getC(double lat, double lng) {
        return new Coordinate(lng, lat);
    }

    public static Coordinate get(double lng, double lat) {
        return new Coordinate(lng, lat);
    }

    public static void test1() throws ParseException {
        // 实际距离(来自在线距离计算): 964.4161905796963m
        // 中心经纬度是指什么?
        System.out.println(distance(110.17863, 44.898857, 110.170822, 44.892169));
        // 这个比较准
        System.out.println(GetDistance(110.17863, 44.898857, 110.170822, 44.892169));
        Coordinate c1 = new Coordinate(110.17863, 44.898857);
        Coordinate c2 = new Coordinate(110.170822, 44.892169);

        System.out.println(c1.distance(c2));
        System.out.println();

        WKTReader wktReader = new WKTReader();
        PointPairDistance pointPairDistance = new PointPairDistance();
        // 怡思苑 -> 南十里居
//        LineString line = (LineString) wktReader.read("LINESTRING (116.498699 39.963286, 116.503523 39.962876)");
        // 南十里居 -> 东枫德必WE
//        LineString line = (LineString) wktReader.read("LINESTRING (116.503523 39.962876, 116.512353 39.95885)");
        // 瞰都嘉园 ->  东枫德必WE
        LineString line = (LineString) wktReader.read("LINESTRING (116.492806 39.96735, 116.512353 39.95885)");
        System.out.println(line.getNumPoints());
        Point point = (Point) wktReader.read("POINT (116.495752 39.958032)");
        DistanceToPoint.computeDistance(line, point.getCoordinate(),pointPairDistance);

        // 这个可以得到点到线的最短距离（不会有延长线，就是说如果夹角是锐角，那么不会有垂足）
        System.out.println(pointPairDistance.toString());
        //LINESTRING ( 115.80597777855581 39.257218662582055, 115.805976 39.257228 )
    }
}
