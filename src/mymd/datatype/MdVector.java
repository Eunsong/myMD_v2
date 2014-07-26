package mymd.datatype;

public interface MdVector{

	public static MdVector create();

    public MdVector();

    public MdVector(double x, double y, double z);

    public MdVector(MdVector vec);

	public double getX();
	public double getY();
	public double getZ();

    public double norm();

    public double normsq();

    public void copy(MdVector vec);

	public void copy(double x, double y, double z);

    public void sub(MdVector vec);

    public void add(MdVector vec);


	/******** Static methods ********/

    public static double norm(MdVector vec);

    public static MdVector sub(MdVector v1, MdVector v2);

    public static MdVector add(MdVector v1, MdVector v2);

    // three vector addition
    public static MdVector add(MdVector v1, MdVector v2, MdVector v3);

    public static double dot(MdVector v1, MdVector v2);

    public static MdVector cross(MdVector v1, MdVector v2);

    public MdVector times(double factor);

    public MdVector times(int factor);





}
