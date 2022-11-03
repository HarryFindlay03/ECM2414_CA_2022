import random

num_files = int(input("Enter number of files to generate: "))

#How many files to generate
for x in range(0, num_files + 1):
    file_name = str(x) + ".txt"
    with open(file_name, "w+") as f:
        nums_to_gen = x * 8
        for i in range(nums_to_gen):
            n = random.randint(1, 16)
            if i == nums_to_gen - 1:
                f.write(str(n))
            else:
                f.write(str(n) + "\n")