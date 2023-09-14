import argparse
from pathlib import Path
import re
import numpy as np

file = Path("HumanBody.xml")

def extract_unique_number_values(filepath: str, xml_attribute: str) -> np.ndarray:
    # Construct filepath + initialize container arr
    filepath = Path(filepath)
    xml_attr_vals = []
    
    # Extract unique values of XML attribute in file
    with open(file, "r") as f:
        for line in f.readlines():
            clean_line = line.rstrip()
            pattern = f'{xml_attribute}="(\d+|\d+.\d+)"'
            match = re.search(pattern, clean_line)
            if match:
                xml_attr_vals.append(match.group(1))

    unique_xml_attr_vals = np.sort(np.unique(xml_attr_vals))
    return unique_xml_attr_vals
    

def main():
    parser = argparse.ArgumentParser(description="Get unique number values for an XML attribute")

    # Filepath to XML
    parser.add_argument("filepath", type=str, help="Path to the XML file")
    parser.add_argument("xml_attribute", type=str, help="The XML attribute to fetch the int or float value")

    # Get the arguments and extract values of xml_attr
    args = parser.parse_args()
    unique_vals = extract_unique_number_values(args.filepath, args.xml_attribute)
    print(f"Possible values for {args.xml_attribute}: {unique_vals}")
if __name__ == "__main__":
    main()