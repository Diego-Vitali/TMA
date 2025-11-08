import pandas as pd
import numpy as np
import sklearn as sk
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_absolute_error, mean_squared_error, r2_score
import matplotlib.pyplot as plt
import seaborn as sns
import logging as lg
import os

# TO-DO
    # [ ] Adicionar logs externos para validação mais robusta
    # [ ] Adicionar tratamento de erros
    # [ ] Adicionar testes unitários
    # [ ] Testar principais número do cep x UF

# CONSTANTES
path_dir = fr"C:\Users\A0172101\OneDrive - Telefonica\Gui Ludgero\_Python\ifsp\TCC"
filename = "Base de dados 2024 - Sudeste.xlsx"
sheetname = "STDIN"
delete_cols = ['CEP destinatário NF', 'Cidade emitente NF', 'Código IBGE emitente NF', "CEP emitente NF", 'Cidade destinatário NF', 'Código IBGE destinatário NF', 'Código tipo de frete NF', 'Peso aferido', 'Data de expedição', 'Data real da entrega']
cat_cols = ['Tipo de frete NF', 'Via de transporte', "UF emitente NF", "UF destinatário NF"]
num_cols = ["Peso total bruto", "Metro cúbico", "Valor NF", "Volume NF"]
target = "transit time"

def sep():
    print("\n\n")

def get_dataset(path_dir, filename, sheetname):
    full_path = os.path.join(path_dir, filename)
    df = pd.read_excel(full_path, sheet_name=sheetname)
    return df

def check_null(df): # Verifica a porcentagem de valores nulos no dataset
    print("Representatividade dos valores nulos:")
    sep()
    print((df.isnull().sum() / df.shape[0])*100)

def get_corr_mtrx(df):
    corr = df.corr()
    plt.figure()
    sns.heatmap(corr, annot=True, fmt=".1f", cmap="coolwarm")
    plt.show()

def expo_analysis(df): # Análise exploratória do dataframe
    rows = df.shape[0]
    cols = df.shape[1]
    print(f"O Dataset inputado tem {rows} linhas e {cols} colunas.")
    sep()
    check_null(df)
    sep()
    print("As colunas do df tem o seguinte dtype:")
    sep()
    print(df.info())
    sep()
    print(df.head())
    sep()

def get_tma(df):
    df["transit time"] = df['Data real da entrega'] - df['Data de expedição']
    return df

def get_iqr(y):
    q1, q3 = np.percentile(y, [25, 75])
    iqr = q3 - q1
    return iqr, q1, q3

def drop_outliers(df):
    iqr, q1, q3 = get_iqr(df['transit time'])
    inf_l = q1 - 1.5 * iqr
    sup_l = q3 + 1.5 * iqr
    df_clean = df[(df["transit time"] >= inf_l) & (df["transit time"] <= sup_l)]
    return df_clean

def apply_ohe(df, cat_cols):
    df_ohe = pd.get_dummies(df, columns=cat_cols)
    print(df_ohe.info())
    return df_ohe

def df_preprocessing(df, delete_cols, cat_cols): # Pré processamento de dados
    df_preprocessed = df.copy() # Copia o objeto DataFrame pra evitar erros de referência
    df_preprocessed = get_tma(df_preprocessed)
    df_preprocessed.drop(columns=delete_cols, inplace=True, axis=1, errors="ignore") # Dropando colunas que não acrescentam nada
    df_preprocessed.dropna(inplace=True) # Como são pouquíssimos dados nulos não há impacto apagar as linhas nulas
    df_clean = drop_outliers(df_preprocessed)
    df_ohe = apply_ohe(df_clean, cat_cols)
    return df_ohe

def get_X(df, target):
    x = df.drop(columns=[target], axis=1)
    return x

def get_y(df, target):
    y = df[target]
    return y

def get_train_test_set(X, y):
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
    return X_train, X_test, y_train, y_test

def get_num_df(X_train, X_test, num_cols):
    X_train_numeric = X_train[num_cols]
    X_test_numeric = X_test[num_cols]
    return X_train_numeric, X_test_numeric

def get_std_df(X_train, X_test, num_cols):
    scaler = StandardScaler()
    X_train_numeric, X_test_numeric = get_num_df(X_train, X_test, num_cols)
    scaler.fit(X_train_numeric)
    X_train_scaled = scaler.transform(X_train_numeric)
    X_test_scaled = scaler.transform(X_test_numeric)
    X_train[num_cols] = X_train_scaled
    X_test[num_cols] = X_test_scaled
    return X_train, X_test

def get_LR():
    LR = LinearRegression()
    return LR

def fit_model(X_train, y_train, model):
    model.fit(X_train, y_train)
    return model

def predict(X_test, model):
    y_pred = model.predict(X_test)
    return y_pred

def get_metrics(y_test, y_pred):    
    mae = mean_absolute_error(y_test, y_pred)
    mse = mean_squared_error(y_test, y_pred)
    rmse = np.sqrt(mse)
    r2 = r2_score(y_test, y_pred)
    return mae, mse, rmse, r2

def main(path_dir, filename, sheetname, delete_cols, cat_cols, target, num_cols):
    df_original = get_dataset(path_dir, filename, sheetname)
    #expo_analysis(df_original)
    df_preprocessed = df_preprocessing(df_original, delete_cols, cat_cols)
    X = get_X(df_preprocessed, target)
    y = get_y(df_preprocessed, target)
    X_train, X_test, y_train, y_test = get_train_test_set(X, y)
    X_train_scaled, X_test_scaled = get_std_df(X_train, X_test, num_cols)
    LR = get_LR()
    LR_trained = fit_model(X_train_scaled, y_train, LR)
    y_pred = predict(X_test_scaled, LR_trained)
    mae, mse, rmse, r2 = get_metrics(y_test, y_pred)
    tma_mean = df_preprocessed[target].mean()
    sep()
    print(f"A média do TMA da base inputada é de: {tma_mean:.2f}")
    print(f"Mean Absolute Error (MAE): {mae:.2f}")
    print(f"Mean Squared Error (MSE): {mse:.2f}")
    print(f"Root Mean Squared Error (RMSE): {rmse:.2f}")
    print(f"R-squared (R²): {r2:.2f}")





    



main(path_dir, filename, sheetname, delete_cols, cat_cols, target, num_cols)