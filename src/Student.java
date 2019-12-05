
/**
 *  Represents a student record object
 *
 *  @author ajd29
 *  @author collee57
 *  @version Nov 27, 2019
 */
public class Student implements Comparable<Student>
   {
       // student's first name
       private String firstName;

       // student's middle name
       private String middleName;

       // student's last name
       private String lastName;

       // student's PID
       private String   pID;

       // student's score
       private int    score;

       // student's grade
       private String grade;

       // essay associated with student
       private String essay;

       // memory handle for student's name
       private MemoryHandle nameHandle;

       // memory handle for student's essay
       private MemoryHandle essayHandle;



       /**
        * parameterized constructor for student object
        * @param p PID
        * @param name full name of student
        */
       public Student(String p, String name)
       {
           firstName = "";
           lastName = "";
           score = 0;
           pID = p;
           grade = "";
           middleName = "";
           essay = "";

           String[] names = name.split(" ");

           if (names.length > 0) {
               firstName = names[0];
           }
           if (names.length > 2) {
               middleName = names[1];
               lastName = names[2];
           }
           if (names.length == 2) {
               lastName = names[1];
           }

           if (firstName.length() == 1)
           {
               firstName = firstName.toUpperCase();
           }

           if (lastName.length() == 1)
           {
               lastName = lastName.toUpperCase();
           }

           if (firstName.length() > 1)
           {
               firstName = firstName.substring(0, 1).toUpperCase()
                   + firstName.substring(1).toLowerCase();
           }

           if (lastName.length() > 1)
           {
               lastName = lastName.substring(0, 1).toUpperCase()
                   + lastName.substring(1).toLowerCase();
           }
       }

       /**
        * Returns name memory handle
        * @return MemoryHandle name memory handle
        */
       public MemoryHandle getNameHandle() {
           return nameHandle;
       }

       /**
        * Returns essay memory handle
        * @return MemoryHandle essay memory handle
        */
       public MemoryHandle getEssayHandle() {
           return essayHandle;
       }

       /**
        * Set name memory handle
        * @param pos position
        * @param len length
        */
       public void setNameHandle(int pos, int len) {
           nameHandle = new MemoryHandle(pos, len);
       }

       /**
        * Set essay memory handle
        * @param pos position
        * @param len length
        */
       public void setEssayHandle(int pos, int len) {
           essayHandle = new MemoryHandle(pos, len);
       }

       /**
        * Returns first name
        *
        * @return String first name
        */
       public String getFirstName()
       {
           return firstName;
       }

       /**
        * Returns middle name
        *
        * @return String middle name
        */
       public String getMiddleName()
       {
           return middleName;
       }

       /**
        * Returns last name
        *
        * @return String last name
        */
       public String getLastName()
       {
           return lastName;
       }

       /**
        * Returns string of full name
        *
        * @return String representation of full name
        */
       public String getName()
       {
           return firstName + " " + lastName;
       }

       /**
        * Sets student's name
        * @param newName to set name to
        */
       public void setName(String newName) {
           String[] name = newName.split(" ");
           if (name.length > 0) {
               firstName = name[0];
           }
           if (name.length > 2) {
               middleName = name[1];
               lastName = name[2];
           }
           if (name.length == 2) {
               lastName = name[1];
           }
       }

       /**
        * Returns string of essay
        * @return String representation of essay
        */
       public String getEssay() {
           return essay;
       }

       /**
        * Sets a student's essay
        * @param stuEssay to set student's essay to
        */
       public void setEssay(String stuEssay) {
           essay = stuEssay;
       }

       /**
        * Returns the student's score
        *
        * @return in student's score
        */
       public int getScore()
       {
           return score;
       }

       /**
        * Returns the student's PID
        *
        * @return String PID
        */
       public String getPID()
       {
           return pID;
       }

       /**
        * setter for score
        *
        * @param num
        *            score to set to
        */
       public void setScore(int num)
       {
           if (num >= 0 && num <= 100)
           {
               score = num;
           }
       }

       /**
        * getter for the Student's grade
        *
        * @return String representation of student's grade
        */
       public String getGrade()
       {
           return grade;
       }

       /**
        * setter for the Student's grade
        *
        * @param str grade
        */
       public void setGrade(String str)
       {
           grade = str;
       }


       /**
        * setter for id
        *
        * @param i ID
        */
       public void setPID(String i)
       {
           pID = i;
       }


       /**
        * Returns middle name
        *
        * @param mid middle name
        */
       public void setMiddleName(String mid)
       {
           middleName = mid;
       }


       /**
        * Compares a student to another student based on name
        *
        * @param obj
        *            object to compare to
        * @return int 1 if student is greater, -1 if student is less than and 0 if
        *         students are equal
        */
       @Override
       public int compareTo(Student obj)
       {
           Student student = obj;

           if (this.getLastName().equalsIgnoreCase(student.getLastName()))
           {
               if (this.getFirstName().compareTo(student.getFirstName()) < 0)
               {
                   return -1;
               }
               if (this.getFirstName().compareTo(student.getFirstName()) > 0)
               {
                   return 1;
               }
               if (this.getMiddleName().compareTo(student.getMiddleName()) < 0)
               {
                   return -1;
               }
               if (this.getMiddleName().compareTo(student.getMiddleName()) > 0)
               {
                   return 1;
               }
               return 0;

           }

           if (this.getLastName().toLowerCase()
               .compareTo(student.getLastName().toLowerCase()) < 0)
           {
               return -1;
           }
           return 1;

       }

       /**
        * Checks if two student objects are equal based on PID
        *
        * @param obj
        *            object to compare to
        * @return boolean true if students are equal
        */
       public boolean equals(Object obj)
       {
           if (obj == null)
           {
               return false;
           }
           if (obj.getClass().equals(this.getClass())) {

               Student student = (Student)obj;

               return (student.getPID() == this.getPID());
           }
           return false;
       }


       /**
        * Student's toString() method
        *
        * @return String representation of a student record
        */
       public String toString()
       {
           return this.getPID() + ", " + this.getFirstName() + " "
               + this.getLastName() + ", score = " + this.getScore();
       }

   }
