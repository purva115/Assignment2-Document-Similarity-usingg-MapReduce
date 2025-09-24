# Assignment 2: Document Similarity using MapReduce

## Overview
This project implements a document similarity analysis using the MapReduce paradigm. It leverages Hadoop to process large datasets and compute similarity metrics between documents efficiently. The assignment demonstrates distributed computing concepts and practical use of Hadoop for text analytics.

## Features
- Distributed document similarity computation using MapReduce
- Hadoop integration via Docker
- Scalable for large datasets
- Modular code structure

## Prerequisites
- Docker and Docker Compose installed
- Java (for Hadoop)
- Basic knowledge of MapReduce and Hadoop

## Setup
1. **Clone the repository:**
   ```bash
   git clone <repo-url>
   cd Assignment2-Document-Similarity-usingg-MapReduce
   ```
2. **Start Hadoop using Docker Compose:**
   ```bash
   docker-compose up -d
   ```
3. **Configure Hadoop environment:**
   - Edit `hadoop.env` if needed for custom settings.
4. **Build the project:**
   ```bash
   mvn clean package
   ```

## Usage
1. **Prepare your input data:**
   - Place your text files in the appropriate HDFS directory.
2. **Run the MapReduce job:**
   - Submit your job to Hadoop using the built JAR file.
3. **View results:**
   - Output will be available in the specified HDFS output directory.

## Project Structure
- `src/main/`: Java source code for MapReduce jobs
- `docker-compose.yml`: Docker setup for Hadoop
- `hadoop.env`: Hadoop environment variables
- `pom.xml`: Maven build configuration
- `README.md`: Project documentation

---

## Approach and Implementation

### Mapper Design
The Mapper reads each line from the input dataset, where each line represents a document with a unique identifier and its content. The input key-value pair is typically the line offset and the line text. The Mapper tokenizes the document content into words and emits key-value pairs representing document-word relationships. For document similarity, the Mapper may also generate all possible document pairs and their associated word sets. This helps in preparing the data for pairwise comparison in the Reducer.

### Reducer Design
The Reducer receives document pairs as keys and lists of word sets as values. It processes these values to compute the intersection and union of word sets for each document pair. The final output is the document pair and their Jaccard Similarity, calculated as:

    Jaccard Similarity = (Number of common words) / (Total unique words)

The Reducer emits the document pair and their similarity score as the final output.

### Overall Data Flow
1. Input files are read line by line by the Mapper.
2. The Mapper emits key-value pairs for document pairs and their word sets.
3. During the shuffle and sort phase, all values for the same document pair are grouped together.
4. The Reducer processes each group, calculates the Jaccard Similarity, and emits the result.
5. The output is written to HDFS for retrieval.

## Challenges and Solutions
- **Pair Generation:** Generating all unique document pairs efficiently was challenging. This was solved by using appropriate data structures and logic in the Mapper.
- **Debugging in Hadoop:** Debugging distributed jobs can be difficult. Used logging and small test datasets to verify correctness.
- **Data Transfer:** Moving data between local, Docker, and HDFS required careful command usage and path management.
- **Output Directory Errors:** Hadoop jobs fail if the output directory exists. Changed output directory names to avoid errors.

## Setup and Execution
**Note:** Edit the commands as needed for your assignment.

1. **Start the Hadoop Cluster**
   ```bash
   docker compose up -d
   ```
2. **Build the Code**
   ```bash
   mvn clean package
   ```
3. **Copy JAR to Docker Container**
   ```bash
   docker cp target/WordCountUsingHadoop-0.0.1-SNAPSHOT.jar resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
   ```
4. **Move Dataset to Docker Container**
   ```bash
   docker cp shared-folder/input/data/input.txt resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
   ```
5. **Connect to Docker Container**
   ```bash
   docker exec -it resourcemanager /bin/bash
   cd /opt/hadoop-3.2.1/share/hadoop/mapreduce/
   ```
6. **Set Up HDFS**
   ```bash
   hadoop fs -mkdir -p /input/data
   hadoop fs -put ./input.txt /input/data
   ```
7. **Execute the MapReduce Job**
   ```bash
   hadoop jar /opt/hadoop-3.2.1/share/hadoop/mapreduce/WordCountUsingHadoop-0.0.1-SNAPSHOT.jar com.example.controller.Controller /input/data/input.txt /output1
   ```
   *(Change output folder name if it already exists)*
8. **View the Output**
   ```bash
   hadoop fs -cat /output1/*
   ```
9. **Copy Output from HDFS to Local OS**
   ```bash
   hdfs dfs -get /output1 /opt/hadoop-3.2.1/share/hadoop/mapreduce/
   exit
   docker cp resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/output1/ shared-folder/output/
   ```
10. **Commit and Push**
    Commit and push your results to the repository.

## Sample Input
**Input from small_dataset.txt**
```
Document1 This is a sample document containing words
Document2 Another document that also has words
Document3 Sample text with different words
```

## Sample Output
**Output from small_dataset.txt**
```
"Document1, Document2 Similarity: 0.56"
"Document1, Document3 Similarity: 0.42"
"Document2, Document3 Similarity: 0.50"
```

**Output from medium_dataset.txt**
```
Document14, Document15	Similarity: 0.10
Document14, Document12	Similarity: 0.03
Document14, Document13	Similarity: 0.05
Document14, Document10	Similarity: 0.11
Document14, Document11	Similarity: 0.06
Document14, Document20	Similarity: 0.04
Document14, Document9	Similarity: 0.07
Document14, Document8	Similarity: 0.08
Document14, Document7	Similarity: 0.05
Document14, Document6	Similarity: 0.04
Document14, Document5	Similarity: 0.06
Document14, Document4	Similarity: 0.05
Document14, Document3	Similarity: 0.13
Document14, Document2	Similarity: 0.05
Document14, Document1	Similarity: 0.06
Document14, Document18	Similarity: 0.11
Document14, Document19	Similarity: 0.04
Document14, Document16	Similarity: 0.12
Document14, Document17	Similarity: 0.04
Document15, Document12	Similarity: 0.04
Document15, Document13	Similarity: 0.09
Document15, Document10	Similarity: 0.08
Document15, Document11	Similarity: 0.06
Document15, Document20	Similarity: 0.08
Document15, Document9	Similarity: 0.07
Document15, Document8	Similarity: 0.11
Document15, Document7	Similarity: 0.10
Document15, Document6	Similarity: 0.09
Document15, Document5	Similarity: 0.04
Document15, Document4	Similarity: 0.11
Document15, Document3	Similarity: 0.09
Document15, Document2	Similarity: 0.05
Document15, Document1	Similarity: 0.07
Document15, Document18	Similarity: 0.08
Document15, Document19	Similarity: 0.06
Document15, Document16	Similarity: 0.09
Document15, Document17	Similarity: 0.07
Document12, Document13	Similarity: 0.08
Document12, Document10	Similarity: 0.09
Document12, Document11	Similarity: 0.05
Document12, Document20	Similarity: 0.16
Document12, Document9	Similarity: 0.17
Document12, Document8	Similarity: 0.12
Document12, Document7	Similarity: 0.06
Document12, Document6	Similarity: 0.07
Document12, Document5	Similarity: 0.13
Document12, Document4	Similarity: 0.04
Document12, Document3	Similarity: 0.10
Document12, Document2	Similarity: 0.07
Document12, Document1	Similarity: 0.02
Document12, Document18	Similarity: 0.05
Document12, Document19	Similarity: 0.07
Document12, Document16	Similarity: 0.07
Document12, Document17	Similarity: 0.15
Document13, Document10	Similarity: 0.10
Document13, Document11	Similarity: 0.07
Document13, Document20	Similarity: 0.09
Document13, Document9	Similarity: 0.14
Document13, Document8	Similarity: 0.07
Document13, Document7	Similarity: 0.08
Document13, Document6	Similarity: 0.08
Document13, Document5	Similarity: 0.07
Document13, Document4	Similarity: 0.06
Document13, Document3	Similarity: 0.09
Document13, Document2	Similarity: 0.11
Document13, Document1	Similarity: 0.02
Document13, Document18	Similarity: 0.05
Document13, Document19	Similarity: 0.06
Document13, Document16	Similarity: 0.13
Document13, Document17	Similarity: 0.09
Document10, Document11	Similarity: 0.05
Document10, Document20	Similarity: 0.13
Document10, Document9	Similarity: 0.09
Document10, Document8	Similarity: 0.11
Document10, Document7	Similarity: 0.09
Document10, Document6	Similarity: 0.07
Document10, Document5	Similarity: 0.07
Document10, Document4	Similarity: 0.06
Document10, Document3	Similarity: 0.08
Document10, Document2	Similarity: 0.06
Document10, Document1	Similarity: 0.03
Document10, Document18	Similarity: 0.09
Document10, Document19	Similarity: 0.06
Document10, Document16	Similarity: 0.12
Document10, Document17	Similarity: 0.08
Document11, Document20	Similarity: 0.06
Document11, Document9	Similarity: 0.05
Document11, Document8	Similarity: 0.08
Document11, Document7	Similarity: 0.06
Document11, Document6	Similarity: 0.08
Document11, Document5	Similarity: 0.03
Document11, Document4	Similarity: 0.06
Document11, Document3	Similarity: 0.04
Document11, Document2	Similarity: 0.06
Document11, Document1	Similarity: 0.06
Document11, Document18	Similarity: 0.03
Document11, Document19	Similarity: 0.06
Document11, Document16	Similarity: 0.09
Document11, Document17	Similarity: 0.04
Document20, Document9	Similarity: 0.14
Document20, Document8	Similarity: 0.05
Document20, Document7	Similarity: 0.09
Document20, Document6	Similarity: 0.09
Document20, Document5	Similarity: 0.12
Document20, Document4	Similarity: 0.05
Document20, Document3	Similarity: 0.11
Document20, Document2	Similarity: 0.11
Document20, Document1	Similarity: 0.03
Document20, Document18	Similarity: 0.06
Document20, Document19	Similarity: 0.06
Document20, Document16	Similarity: 0.12
Document20, Document17	Similarity: 0.15
Document9, Document8	Similarity: 0.07
Document9, Document7	Similarity: 0.06
Document9, Document6	Similarity: 0.07
Document9, Document5	Similarity: 0.10
Document9, Document4	Similarity: 0.04
Document9, Document3	Similarity: 0.07
Document9, Document2	Similarity: 0.08
Document9, Document1	Similarity: 0.02
Document9, Document18	Similarity: 0.06
Document9, Document19	Similarity: 0.07
Document9, Document16	Similarity: 0.11
Document9, Document17	Similarity: 0.17
Document8, Document7	Similarity: 0.10
Document8, Document6	Similarity: 0.14
Document8, Document5	Similarity: 0.04
Document8, Document4	Similarity: 0.06
Document8, Document3	Similarity: 0.08
Document8, Document2	Similarity: 0.05
Document8, Document1	Similarity: 0.03
Document8, Document18	Similarity: 0.17
Document8, Document19	Similarity: 0.06
Document8, Document16	Similarity: 0.10
Document8, Document17	Similarity: 0.07
Document7, Document6	Similarity: 0.13
Document7, Document5	Similarity: 0.03
Document7, Document4	Similarity: 0.07
Document7, Document3	Similarity: 0.09
Document7, Document2	Similarity: 0.08
Document7, Document1	Similarity: 0.04
Document7, Document18	Similarity: 0.09
Document7, Document19	Similarity: 0.05
Document7, Document16	Similarity: 0.08
Document7, Document17	Similarity: 0.07
Document6, Document5	Similarity: 0.04
Document6, Document4	Similarity: 0.03
Document6, Document3	Similarity: 0.06
Document6, Document2	Similarity: 0.07
Document6, Document1	Similarity: 0.04
Document6, Document18	Similarity: 0.05
Document6, Document19	Similarity: 0.05
Document6, Document16	Similarity: 0.09
Document6, Document17	Similarity: 0.06
Document5, Document4	Similarity: 0.03
Document5, Document3	Similarity: 0.09
Document5, Document2	Similarity: 0.08
Document5, Document1	Similarity: 0.02
Document5, Document18	Similarity: 0.04
Document5, Document19	Similarity: 0.05
Document5, Document16	Similarity: 0.09
Document5, Document17	Similarity: 0.10
Document4, Document3	Similarity: 0.05
Document4, Document2	Similarity: 0.06
Document4, Document1	Similarity: 0.04
Document4, Document18	Similarity: 0.08
Document4, Document19	Similarity: 0.06
Document4, Document16	Similarity: 0.04
Document4, Document17	Similarity: 0.04
Document3, Document2	Similarity: 0.05
Document3, Document1	Similarity: 0.03
Document3, Document18	Similarity: 0.11
Document3, Document19	Similarity: 0.04
Document3, Document16	Similarity: 0.06
Document3, Document17	Similarity: 0.06
Document2, Document1	Similarity: 0.03
Document2, Document18	Similarity: 0.05
Document2, Document19	Similarity: 0.04
Document2, Document16	Similarity: 0.17
Document2, Document17	Similarity: 0.10
Document1, Document18	Similarity: 0.03
Document1, Document19	Similarity: 0.02
Document1, Document16	Similarity: 0.05
Document1, Document17	Similarity: 0.02
Document18, Document19	Similarity: 0.04
Document18, Document16	Similarity: 0.03
Document18, Document17	Similarity: 0.03
Document19, Document16	Similarity: 0.07
Document19, Document17	Similarity: 0.03
Document16, Document17	Similarity: 0.10

```
## License
This project is licensed under the MIT License.

## Contact
For questions, contact the course instructor or open an issue in the repository.
