package org.example;


import org.locationtech.jts.algorithm.distance.DistanceToPoint;
import org.locationtech.jts.algorithm.distance.PointPairDistance;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

/**
 * @description:
 * @author：kun.wang
 * @date: 2022/12/21
 * @assert: 自律自律自律
 */
public class GeoTool {

    /**
     * 默认地球半径
     */
    private static double EARTH_RADIUS = 6371000;//赤道半径(单位m)

    /**
     * 转化为弧度(rad)
     * */
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    /**
     * @param lon1 第一点的精度
     * @param lat1 第一点的纬度
     * @param lon2 第二点的精度
     * @param lat2 第二点的纬度
     * @return 返回的距离，单位m
     * */
    public static double GetDistance(double lon1,double lat1,double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    /**
     * @return 返回的距离，单位m
     * */
    public static double GetDistance(Point point1, Point point2) {
        return GetDistance(point1.getX(), point1.getY(), point2.getX(), point2.getY());
    }

    /**
     * 计算中心经纬度与目标经纬度的距离（米）
     *
     * @param centerLon
     *            中心精度
     * @param centerLat
     *            中心纬度
     * @param targetLon
     *            需要计算的精度
     * @param targetLat
     *            需要计算的纬度
     * @return 米
     */
    static double distance(double centerLon, double centerLat, double targetLon, double targetLat) {

        double jl_jd = 102834.74258026089786013677476285;// 每经度单位米;
        double jl_wd = 111712.69150641055729984301412873;// 每纬度单位米;
        double b = Math.abs((centerLat - targetLat) * jl_jd);
        double a = Math.abs((centerLon - targetLon) * jl_wd);
        return Math.sqrt((a * a + b * b));
    }

    /**
     * 点面相交
     * @param point
     * @param polygon
     * @return
     */
    public static boolean intersectsPointAndPolygon(Point point, Polygon polygon) {
        return polygon.intersects(point);
    }

    /**
     * 线面相交
     * @param line
     * @param polygon
     * @return
     */
    public static boolean intersectsLineAndPolygon(LineString line, Polygon polygon) {
        return polygon.intersects(line);
    }

    /**
     * 线线相交
     * @param line1
     * @param line2
     * @return
     */
    public static boolean intersectsLineAndLine(LineString line1, LineString line2) {
        return line1.intersects(line2);
    }


    /**
     * 线圆相交
     * @param line
     * @param circle
     * @return
     */
    public static boolean intersectsLineAndCircle(LineString line, Circle circle) throws ParseException {
        GeometryFactory geometryFactory = new GeometryFactory();

        Point center = geometryFactory.createPoint(new Coordinate(circle.getLng(), circle.getLat()));
        int len = line.getNumPoints();
        // 先判断每个点是否在圆内
        for (int i = 0; i < len; i++) {
            // 判断点的距离
            double distance = distanceForPointToPoint(center, line.getPointN(i));
            if (distance < circle.getRadius()) {
                return true;
            }
            // 判断线的距离
            if (i > 0) {
                LineString tempLine = geometryFactory.createLineString(new Coordinate[]{line.getCoordinateN( i - 1 ), line.getCoordinateN(i)});
                double distance2 = distanceForPointToLine(center, tempLine);
                if (distance2 < circle.getRadius()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 点到点的距离
     * @param point1
     * @param point2
     * @return
     */
    public static double distanceForPointToPoint(Point point1, Point point2) {
        return GetDistance(point1, point2);
    }

    /**
     * 点到线的距离-垂直
     * @param point
     * @param line
     * @return
     */
    public static double distanceForPointToLine(Point point, LineString line) throws ParseException {
        PointPairDistance pointPairDistance = new PointPairDistance();

        WKTReader wktReader = new WKTReader();

        DistanceToPoint.computeDistance(line, point.getCoordinate(),pointPairDistance);

        LineString shortestLine = (LineString) wktReader.read(pointPairDistance.toString());

        //LINESTRING ( 115.80597777855581 39.257218662582055, 115.805976 39.257228 )
        return GetDistance(shortestLine.getStartPoint(), shortestLine.getEndPoint());
    }




}
