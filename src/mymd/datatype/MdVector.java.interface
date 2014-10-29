package mymd.datatype;

public interface MdVector{

	public static MdVector create();

	public static MdVector create(MdVector vec);

    //public MdVector();

    //public MdVector(double x, double y, double z);

    //public MdVector(MdVector vec);

	public double getX();
	public double getY();
	public double getZ();

    public double norm();

    public double normsq();

    public void copy(MdVector vec);

	public void copy(double x, double y, double z);

    public void sub(MdVector vec);

    public void add(MdVector vec);
	public void add(MdVector vec1, MdVector vec2);
	public void add(MdVector vec1, MdVector vec2, MdVector vec3);

	public void times(double factor);
	public void times(int factor);


	// set to 0 vector	
	public void reset();


	/******** Static methods ********/

    public static double norm(MdVector vec);

    public static MdVector sub(MdVector v1, MdVector v2);

    public static MdVector add(MdVector v1, MdVector v2);

    // three vector addition
    public static MdVector add(MdVector v1, MdVector v2, MdVector v3);

    public static double dot(MdVector v1, MdVector v2);

    public static MdVector cross(MdVector v1, MdVector v2);

    public static MdVector times(double factor, MdVector v);
    public static MdVector times(MdVector v, double factor);

    public static MdVector times(int factor, MdVector v);
	public static MdVector times(MdVector v, int factor);


}
