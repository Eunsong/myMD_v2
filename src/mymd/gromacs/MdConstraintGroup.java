package mymd.gromacs;

import java.util.*;

public class MdConstraintGroup{

    private List<MdConstraint> constraintGroup;

    public  MdConstraintGroup(){
        this.constraintGroup = new ArrayList<MdConstraint>();
    }

    public void addConstraint(MdConstraint constraint){
        this.constraintGroup.add(constraint);
    }
    
    public List<MdConstraint> getConstraints(){
        return this.constraintGroup;
    }

    public MdConstraint getConstraint(int i){
        return this.constraintGroup.get(i);
    }

}
