import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Demo {

    ArrayList<Company> companies = new ArrayList<Company>();

    public final static class Interval implements Comparable<Interval> {

        public final int a;
        public final int b;

        public Interval(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Interval interval = (Interval) o;
            return a == interval.a && b == interval.b;
        }

        @Override
        public int hashCode() {
            int result = a;
            result = 31 * result + b;
            return result;
        }

        public boolean isConflict(Interval x) {
            return x.equals(this) || !(x.b < this.a || x.a > this.b);
        }

        @Override
        public String toString() {
            return "{a:" + a + ", b:" + b + '}';
        }

        public int compareTo(Interval o) {
            if (this.equals(o)) {
                return 0;
            } else {
                if (this.a == o.a) {
                    if (this.b > o.b) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else {
                    if (this.a > o.a) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }
        }
    }

    public final static class Company implements Comparable<Company> {
        public final int id;
        public final Interval interval;
        public final List<Company> relationCompany = new ArrayList<Company>();

        private int depth = 0;

        public int getDepth() {
            return depth;
        }

        public void setDepth(int depth) {
            this.depth = depth;
        }

        public Company(int ID, Interval interval) {
            id = ID;
            this.interval = interval;
        }

        @Override
        public String toString() {
            return "{id:" + id + ",depth:" + depth + ",relations:" + relationCompany + "}";
        }

        public int compareTo(Company o) {
            return id - o.id;
        }
    }


    /**
     * 会议中心
     *
     * @param nCompany?参加申请的公司数
     * @param pCompanyRequestFilePath 每个公司请求使用的起止时间所保存的文件
     * @param pArrIndex               安排公司的索引数组（出参）
     * @return nCompanyOut 正常时返回最多可以安排的公司个数，失败时返回-1
     */
    public long conferenceArrangement(long nCompany, String pCompanyRequestFilePath, List<Integer> pArrIndex) {
        pArrIndex.clear();
        companies.clear();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(pCompanyRequestFilePath));
            int index = 1;
            while (true) {
                String rs = reader.readLine();

                if (rs != null || index > 200000) {
                    Interval interval = new Interval(Integer.parseInt(rs.split(" ")[0]),
                            Integer.parseInt(rs.split(" ")[1]));

                    companies.add(new Company(index, interval));
                } else {
                    break;
                }
                index++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<Company> vertex = new HashSet<Company>();

        for (int i = 0; i < companies.size(); i++) {
            for (int j = i + 1; j < companies.size(); j++) {
                if (!companies.get(i).interval.isConflict(companies.get(j).interval)) {
                    vertex.add(companies.get(i));
                    companies.get(i).relationCompany.add(companies.get(j));
                }
            }
        }

        if (vertex.size() == 0) {
            return -1;
        }

        List<Company> vertex_order = new ArrayList<Company>(vertex);

        Collections.sort(vertex_order);

        for (Company company : vertex_order) {
            orderChildren(company);
            company.setDepth(calculateDepth(company));
        }

        Collections.sort(vertex_order, new Comparator<Company>() {
            public int compare(Company o1, Company o2) {
                return o1.id - o2.id;
            }
        });

        Collections.sort(vertex_order, new Comparator<Company>() {
            public int compare(Company o1, Company o2) {
                return o1.getDepth() - o2.getDepth();
            }
        });

        Company result = vertex_order.get(0);

        calculateFirstChild(result, pArrIndex);

        //System.out.println("result = " + pArrIndex);

        return pArrIndex.size();
    }

    private void calculateFirstChild(Company root, List<Integer> rs) {
        rs.add(root.id);
        if (root.relationCompany.size() > 0) {
            calculateFirstChild(root.relationCompany.get(0), rs);
        }
    }

    private void orderChildren(Company root) {
        Collections.sort(root.relationCompany);
        for (Company company : root.relationCompany) {
            orderChildren(company);
        }
    }

    private int calculateDepth(Company root) {
        int depth = 0;
        if (root.relationCompany.size() > 0) {
            depth++;
        }
        for (Company company : root.relationCompany) {
            depth = depth + calculateDepth(company);
        }
        return depth;
    }
}
