
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * This is a test class used to test the mathematical operations for various 
 * algorithms. By doing "the tests", we will know that our code is 
 * (relatively) correct.
 * @author kieda
 */
public abstract class MathTest {
    static ArrayList<MathTest> testfx = new ArrayList<MathTest>();
    {
        testfx.add(this);
    }
    public static void main(String[] args){
        new DistanceModule();
        
        run();
    }
    abstract void test();
    public static void run(){
        for(MathTest mt : testfx){
            mt.test();
        }
    }
}
/**
 * the test for the algorithms in the distance/relation in the graphs.
 * @author kieda
 */
class DistanceModule extends MathTest{
    //long length of the segment.
    //Or, the distance between p1 and p2
    float lengthL;
    //smaller length of a segment
    float lengthS;
    //the ratio
    float rat;
    
    float distMax = 10;//the maximum distance up the vector can be.
                       //should not make a difference, but still interdatsting.
    
    //calculate the positions of vertices based on the given lengths.
    void calc(){
        assert(lengthS > 0&& lengthL>=lengthS);
        rat = lengthS/lengthL;
        p1 = new Point2D.Float(0, 0);//0,0 to 0, lengthL
        p2 = new Point2D.Float(lengthL, 0);
        
        pp = new Point2D.Float(lengthS, (float)(distMax*Math.random()));
        //out by the small length, up random amounts.
        
        /**
         * 
         *            ^
         *            |
         * <----------0------>
         * < lengthS  >
         * < lengthL         >
         */
        testDistance();
    }
    public Point2D p1;
    public Point2D p2;
    public Point2D pp;
    void set(float l1, float l2){
        assert(l1 > 0&& l1>=l2);
        this.lengthL = l1;
        this.lengthS = l2;
        calc();
    }
    void test(){
        set(14, 5);
        set(2, .9f);
        set(10, 5);
    }
    void testDistance(){
        float p1x = (float)p1.getX(); float p1y = (float)p1.getY();
        float p2x = (float)p2.getX(); float p2y = (float)p2.getY();
        float ppx = (float)pp.getX(); float ppy = (float)pp.getY();
        //<vector>
            p2x -= p1x;
            p2y -= p1y;
        //</vector>

        //<vector>
            ppx -= p1x;
            ppy -= p1y;
        //</vector>
        float aa = 0;//a affects p1, b affects p2
        float dot_prod = ppx*p2x + ppy*p2y;//dot(pp, p2);
        float projlenSq;
        if(dot_prod<=0.0){
            projlenSq = 0.0f;
            //closest to p1
            aa = 1f;
        }else{
            //<vector>
                ppx = p2x - ppx;
                ppy = p2y - ppy;
            //</vector>

            dot_prod = (ppx*p2x + ppy*p2y);
            if(dot_prod<=0.0f){
                projlenSq = 0.0f;
                aa = 0;//closest to p2
            }else{
                float dot_prode = p2y*p2y + (p2x*p2x);
                projlenSq = (dot_prod*dot_prod)/dot_prode;
                aa = (float)Math.sqrt(projlenSq/dot_prode);
                //root(projection length squared /  length of segment squared )
            }
        }
        float lenSq = ppx*ppx + ppy*ppy - projlenSq;
        float bb = 1 - aa;//bb IS the correct length.
        if(lenSq < 0) lenSq = 0;
        System.out.println("l1 "+Math.sqrt(projlenSq));
        System.out.println("l1 "+(lengthL-lengthS));
    }
}