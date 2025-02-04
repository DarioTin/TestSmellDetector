package testsmell;

import com.github.javaparser.ast.CompilationUnit;
import thresholds.Thresholds;

import java.io.FileNotFoundException;
import java.util.*;

public abstract class AbstractSmell {
    protected Thresholds thresholds;
    protected Set<SmellyElement> smellyElementsSet;
    protected Map<String, Set<String>> result;
    protected Map<String, Integer> score;

    public AbstractSmell(Thresholds thresholds) {
        this.thresholds = thresholds;
        this.smellyElementsSet = new HashSet<>();
        this.result = new HashMap<>();
        this.score = new HashMap<>();
    }

    public Map<String,Set<String>> getResult(){
        return this.result;
    }

    public Map<String, Integer> getScore() {
        return score;
    }

    public void putSmellyElement(String element) {
        if(result.containsKey(this.getSmellName())){
            Set<String> list = result.get(this.getSmellName());
            list.add(element);
            this.result.put(this.getSmellName(),list);
        } else {
            Set<String> list = new HashSet<>();
            list.add(element);
            this.result.put(this.getSmellName(),list);
        }
    }

    public void addScore(Integer point){
        if(score.containsKey(this.getSmellName())){
            Integer points = score.get(this.getSmellName());
            points+= point;
            this.score.put(this.getSmellName(),points);
        } else{
            this.score.put(this.getSmellName(),point);
        }
    }


    public abstract String getSmellName();

    /**
     * Return 1 if any of the elements has a smell; 0 otherwise
     */
    public boolean hasSmell() {
        return smellyElementsSet.stream().filter(SmellyElement::isSmelly).count() >= 1;
    }

    public abstract void runAnalysis(CompilationUnit testFileCompilationUnit,
                                     CompilationUnit productionFileCompilationUnit,
                                     String testFileName,
                                     String productionFileName) throws FileNotFoundException;

    /**
     * Returns the set of analyzed elements (i.e. test methods)
     */
    public Set<SmellyElement> getSmellyElements() {
        return smellyElementsSet;
    }

    /**
     * Returns the number of test cases in a test suite (jUnit test file).
     * In theory, it counts all the smelly elements (i.e., the methods), that are smelly
     */
    public int getNumberOfSmellyTests() {
        return (int) smellyElementsSet.stream().filter(SmellyElement::isSmelly).count();
    }
}
