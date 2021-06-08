from typing import Optional, Dict, List

file = "/data/pg1787.txt"

import re

with open(file) as f:
    line: str = f.read()
    words: List[str] = re.findall("[A-Za-z]+", line)

    dictionary: Dict[str, int] = {}

    for word in words:
        w = word.lower()
        try:
            count : Optional[int] = dictionary.get(w)
            if count:
                dictionary[w] += 1
            else:
                dictionary[w] = 1
        except Exception as e:
            dictionary[w] = 1

    i = 0
    for key, value in sorted(dictionary.items(), key=lambda v: -v[1]):
        if i < 20:
            print(key, ' : ', value)
            i+=1
        else:
            break







