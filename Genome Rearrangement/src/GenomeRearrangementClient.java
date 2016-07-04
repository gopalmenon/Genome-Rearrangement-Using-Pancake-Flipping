import java.util.Arrays;
import java.util.List;


public class GenomeRearrangementClient {

	public static void main(String[] args) {
		
		GenomeRearrangementClient genomeRearrangementClient = new GenomeRearrangementClient();
		genomeRearrangementClient.restoreToIdentitySequence();
	}
	
	public void restoreToIdentitySequence() {
		
		//GenomeRearrangement genomeRearrangement = new GenomeRearrangement(10);
		//System.out.println(genomeRearrangement.getGenomicSequence());
		//System.out.println("Number of flipes was " + genomeRearrangement.getNumberOfFlipsToGenerateGenomicSequence());
		
		Integer[] testSequence = {0,1,2,7,6,5,8,4,3,9};
		List<Integer> testGenomicSequence = Arrays.asList(testSequence);
		GenomeRearrangement.getNumberOfFlipsToRevertToIdentitySequence(testGenomicSequence);
		
	}
}
